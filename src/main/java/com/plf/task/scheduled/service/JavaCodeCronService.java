package com.plf.task.scheduled.service;

import com.plf.task.scheduled.utils.ParseJavaCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class JavaCodeCronService {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final static ParseJavaCode compiler = new ParseJavaCode();

    public void startJavaCodeTask(String code,String cron) throws Exception{
        Class<?> cla = compiler.compile(code);
        Runnable obj = (Runnable) cla.getDeclaredConstructor().newInstance();
        threadPoolTaskScheduler.schedule((Runnable) obj,
                new CronTrigger(cron));
    }

}
