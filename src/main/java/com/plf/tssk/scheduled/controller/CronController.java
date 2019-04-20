package com.plf.tssk.scheduled.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plf.tssk.scheduled.bean.TaskBean;
import com.plf.tssk.scheduled.service.CronService;


@RestController
public class CronController {

	@Autowired
	private CronService cronService;
	
	@RequestMapping("/startCronByid")
	public TaskBean startCron(Integer id) {
		TaskBean task=cronService.getCronById(id);
		return task;
	}
}
