package com.plf.task.scheduled.dto;

import java.util.concurrent.ScheduledFuture;

import lombok.Data;

@Data
public class TaskListDto {
	private String cron;
	
	private Object task;
	
	private ScheduledFuture<?> future;
}
