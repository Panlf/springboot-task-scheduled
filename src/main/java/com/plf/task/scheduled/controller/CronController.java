package com.plf.task.scheduled.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plf.task.scheduled.bean.TaskBean;
import com.plf.task.scheduled.service.CronService;


@RestController
public class CronController {

	@Autowired
	private CronService cronService;
	
	@RequestMapping("/startCronByid")
	public TaskBean startCron(Integer id) {
		TaskBean task=cronService.getCronById(id);
		return task;
	}
	
	@RequestMapping("/stopCronByid")
	public String stopCron(Integer id) {
		cronService.stopTaskBean(id);
		return "success";
	}
	
	@RequestMapping("/getJobClass")
	public List<Class<?>> getJobClass(){
		return cronService.getJobClass("com.plf.task.scheduled.job");
	}
}
