package com.plf.task.scheduled.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.service.CronService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api("定时器动态处理的控制器")
@RestController
public class CronController {

	@Autowired
	private CronService cronService;
	
	@ApiOperation("增加定时任务")
	@ApiImplicitParam(name="taskList",value="定时任务属性",required=true,dataTypeClass=TaskList.class)
	@GetMapping("/addCron")
	public String addCron(TaskList taskList){
		cronService.addTaskList(taskList);
		return "success";
	}
	
	@ApiOperation("暂停定时任务")
	@GetMapping("/stopCronByid")
	@ApiImplicitParam(name="id",value="定时任务主键id",required=true,dataTypeClass=Integer.class)
	public String stopCron(Integer id) {
		cronService.cancelTaskList(id);
		return "success";
	}
	
	@ApiOperation("获取所有job包下类的全类名")
	@GetMapping("/getJobClass")
	public List<Class<?>> getJobClass(){
		return cronService.getJobClass("com.plf.task.scheduled.job");
	}
}
