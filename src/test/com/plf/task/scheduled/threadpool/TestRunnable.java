package com.plf.task.scheduled.threadpool;

/**
 * @author panlf
 * @date 2021/5/2
 */
public class TestRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("==========TestRunnable==========");
    }
}
