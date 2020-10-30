##  springboot-task-scheduled
基于SpringBoot和Spring自带的scheduled的定时任务，可以动态修改、删除、增加、暂停、重启定时任务。

### 说明

该定时任务是非分布式，只能单机使用。

### 技术

- SpringBoot 2.1.4 
- Spring-Data-Jpa 
- MySQL

### 功能介绍

目前没有前端页面，只有后端的功能。

- 新增、修改、删除定时任务
- 分页查询定时任务列表
- 获取job包下的全部全类名

### 安装运行

下载本代码，修改application.properties的数据库配置，启动即可。访问`http://localhost:8080/swagger-ui.html`查看控制器的功能。定时任务写在`com.plf.task.scheduled.job`包下，继承`Runnable`编写业务逻辑代码即可。

### 更新说明
1、支持在Runnable任务中进行`Service`调用，可参考`com.plf.task.scheduled.job.MyRunnable2`
2、支持在线代码的编译和定时运行，但是功能所限，不允许引入第三方Jar


