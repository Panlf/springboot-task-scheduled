package com.plf.tssk.scheduled.bean;

import java.util.concurrent.ScheduledFuture;

public class TaskBean {
	private String cron;
	
	private Object task;
	
	private ScheduledFuture<?> future;
	
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public Object getTask() {
		return task;
	}
	public void setTask(Object task) {
		this.task = task;
	}
	public ScheduledFuture<?> getFuture() {
		return future;
	}
	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}
}
