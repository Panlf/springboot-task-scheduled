package com.plf.task.scheduled.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.service.CronService;


@RestController
public class CronController {

	@Autowired
	private CronService cronService;
	
	@RequestMapping("/addCron")
	public String addCron(TaskList taskList){
		cronService.addTaskList(taskList);
		return "success";
	}
	
	@RequestMapping("/stopCronByid")
	public String stopCron(Integer id) {
		cronService.cancelTaskList(id);
		return "success";
	}
	
	@RequestMapping("/getJobClass")
	public List<Class<?>> getJobClass(){
		return cronService.getJobClass("com.plf.task.scheduled.job");
	}
}
