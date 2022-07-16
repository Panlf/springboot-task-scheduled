package com.plf.task.scheduled.job;

/**
 * @author panlf
 * @date 2022/7/16
 */
public abstract class JobTemplate<T> implements Runnable{
    private T t;

    public JobTemplate(){}

    public JobTemplate(T t){this.t = t;}

    public abstract void preHandle();

    public abstract void handle(T t);

    @Override
    public void run(){
        this.preHandle();
        this.handle(t);
        this.postHandle();
    }

    public abstract void postHandle();
}
