package com.plf.task.scheduled.job;

import lombok.extern.slf4j.Slf4j;

/**
 * @author panlf
 * @date 2022/7/16
 */
@Slf4j
public class MyRunnable3 extends JobTemplate<String> {

    public MyRunnable3(){super();}

    public MyRunnable3(String param){
        super(param);
    }

    @Override
    public void preHandle() {
        log.info("前置处理");
    }

    @Override
    public void handle(String s) {
        log.info("处理的数据为 【{}】", s);
    }

    @Override
    public void postHandle() {
        log.info("后置处理");
    }

}
