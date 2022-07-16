package com.plf.task.scheduled.threadpool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

/**
 * @author panlf
 * @date 2021/5/4
 */
public class TestThreadPool {
    public static void main(String[] args) {
        ThreadPoolTaskScheduler testPool = new ThreadPoolTaskScheduler();
        testPool.setPoolSize(20);
        testPool.setThreadNamePrefix("testThreadPoolScheduler-");
        testPool.setWaitForTasksToCompleteOnShutdown(true);
        testPool.setAwaitTerminationSeconds(60);
        testPool.initialize();

        String clazz = "com.plf.task.scheduled.job.MyRunnable3";
        Object obj = null;

        try {
            obj = Class.forName(clazz).getConstructor(String.class).newInstance("亲爱的");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ScheduledFuture<?> future = testPool.schedule((Runnable) obj,
                new CronTrigger("0/2 * * * * ?"));

    }
}
