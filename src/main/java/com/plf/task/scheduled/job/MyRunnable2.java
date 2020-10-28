package com.plf.task.scheduled.job;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.configuration.ApplicationContextProvider;
import com.plf.task.scheduled.service.TaskListService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MyRunnable2 implements Runnable {

	public TaskListService taskListService;

	public MyRunnable2(){
		this.taskListService= ApplicationContextProvider.getBean(TaskListService.class);
	}

	@Override
	public void run() {
		List<TaskList> list = taskListService.findAll();
		log.info("当前的定时任务:{}",list);
	}
}
