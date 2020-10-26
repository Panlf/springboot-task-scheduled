package com.plf.task.scheduled.utils;

import lombok.extern.slf4j.Slf4j;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析JavaCode代码生成实例
 *
 * @author panlf
 * @date 2020/10/24
 */
@Slf4j
public class ParseJavaCode {
    // Java编译器
    private static final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    // 存放编译过程中输出的信息
    private static final DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

    // 内容管理器
    private static final JavaFileManager manager = new CustomJavaFileManager(
            javaCompiler.getStandardFileManager(collector, null, null));

    // 存放编译之后的字节码(key:类全名,value:编译之后输出的字节码)
    private static final Map<String, ByteJavaFileObject> fileObjectMap = new ConcurrentHashMap<>();

    // javac 编译参数
    private static final List<String> options = new ArrayList<>();

    static {
        //options.add("-Xlint:unchecked");
        options.add("-verbose");
        options.add("-source");
        options.add("11");
        options.add("-target");
        options.add("11");
    }

    public Class<?> compile(String code) throws ClassNotFoundException {
        String className = getFullClassName(code);

        //String qualified = className.substring(className.lastIndexOf('.') + 1, className.length());
        // 构建源代码对象
        CustomJavaObject cubeJavaObject = new CustomJavaObject(className, code);

        // 获取一个编译任务
        /*
         * getTask(Writer out,JavaFileManager fileManager, DiagnosticListener
         * diagnosticListener, Iterable options, Iterable classes, Iterable
         * compilationUnits)
         *
         * out:：用于输出错误的流，默认是System.err。 fileManager:：标准的文件管理。 diagnosticListener:
         * 编译器的默认行为。 options: 编译器的选项 classes：参与编译的class。
         * compilationUnits不能为null，因为这个对象保存了你想编译的Java文件。
         *
         *
         */
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, manager, collector, options, null,
                Collections.singletonList(cubeJavaObject));

        // 编译字符串代码
        task.call();

        // 输出诊断信息
        for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
            try {
                log.error("编译错误:{}", diagnostic.toString());
            } catch (Exception e) {
                log.error("输出内容错误", e);
            }
        }
        return new StringClassLoader().findClass(className);
    }

    /**
     * 获取类的全名称
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
    public static String getFullClassName(String sourceCode) {
        String className = "";
        Pattern pattern = Pattern.compile("package\\s+\\S+\\s*;");
        Matcher matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }
        /*
         * 匹配三种格式
         * 1、public class hello extends kky {
         * 2、public class hello implements kky {
         * 3、public class hello {
         */
        pattern = Pattern.compile("class\\s+\\S+\\s+(implements|extends|\\{)");
        matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            // className += matcher.group().replaceFirst("class", "").replace("{",
            // "").trim();
            String matcherStr = matcher.group();
            if (matcherStr.contains("implements")) {
                className += matcher.group().replaceFirst("class", "").replace("implements", "").trim();
            } else if (matcherStr.contains("extends")) {
                className += matcher.group().replaceFirst("class", "").replace("extends", "").trim();
            } else if (matcherStr.contains("{")) {
                className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
            }
        }
        return className;
    }

    /**
     * 自定义类加载器, 用来加载动态的字节码
     */
    private static class StringClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            ByteJavaFileObject fileObject = fileObjectMap.get(name);
            if (fileObject != null) {
                byte[] bytes = fileObject.getCompiledBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
            try {
                return ClassLoader.getSystemClassLoader().loadClass(name);
            } catch (Exception e) {
                log.error("加载类失败,{}", name, e);
                return super.findClass(name);
            }
        }
    }


    /**
     * 自定义一个字符串的源码对象
     */
    private static class CustomJavaObject extends SimpleJavaFileObject {
        //等待编译的源码字段
        private final String contents;

        //java源代码 => StringJavaFileObject对象 的时候使用
        public CustomJavaObject(String className, String contents) {
            super(URI.create("String:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        //字符串源码会调用该方法
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors){
            return contents;
        }
    }

    /**
     * 自定义一个编译之后的字节码对象
     */
    private static class ByteJavaFileObject extends SimpleJavaFileObject {
        //存放编译后的字节码
        private ByteArrayOutputStream outPutStream;

        public ByteJavaFileObject(String className, Kind kind) {
            super(URI.create("String:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), kind);
        }

        //StringJavaFileManage 编译之后的字节码输出会调用该方法（把字节码输出到outputStream）
        @Override
        public OutputStream openOutputStream() {
            outPutStream = new ByteArrayOutputStream();
            return outPutStream;
        }

        //在类加载器加载的时候需要用到
        public byte[] getCompiledBytes() {
            return outPutStream.toByteArray();
        }
    }

    private static class CustomJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        CustomJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        //获取输出的文件对象，它表示给定位置处指定类型的指定类。
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
            fileObjectMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }

    public static void main(String[] args) {
       /* String code = "package com.plf.task.scheduled.utils;"
                + "public class HelloWorld {\n" +
                "    public static void main(String []args) {\n" +
                "\t\tfor(int i=0; i < 10; i++){\n" +
                "\t\t\t       System.out.println(\"Hello World!\");\n" +
                "\t\t}\n" +
                "    }\n" +
                "}";
        ParseJavaCode compiler = new ParseJavaCode();
        try {
            Class<?> cla = compiler.compile(code);
            cla.getDeclaredMethod("main", new Class[]{String[].class}).invoke(null, new Object[]{null});

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String code ="package com.learn;\r\n"
                + "import java.util.Date;\r\n" +
                "\r\n" +
                "public class MyRunnable implements Runnable {\r\n" +
                "	public void run() {\r\n" +
                "		System.out.println(\"任务一----\" + new Date());\r\n" +
                "	}\r\n" +
                "}";
        ParseJavaCode compiler = new ParseJavaCode();
        try {
            Class<?> cla = compiler.compile(code);
            Runnable obj = (Runnable) cla.getDeclaredConstructor().newInstance();
            ScheduledExecutorService scheduledExecutorService =
                    Executors.newScheduledThreadPool(5);

            scheduledExecutorService.scheduleAtFixedRate(obj, 0, 2, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
