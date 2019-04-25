package com.plf.task.scheduled.bean;

public class TaskTableBean {

	private Integer id;
	private String cron;
	private String clazz;
	
	public TaskTableBean(Integer id, String cron, String clazz) {
		super();
		this.id = id;
		this.cron = cron;
		this.clazz = clazz;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
}
