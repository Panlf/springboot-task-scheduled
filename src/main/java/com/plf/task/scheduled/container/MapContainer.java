package com.plf.task.scheduled.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.dto.TaskListDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MapContainer {

	// 静态内部类实现单例模式
	private MapContainer() {
	}

	private static class MapContainerInstance {
		private static final MapContainer INSTANCE = new MapContainer();
	}

	public static MapContainer getInstance() {
		return MapContainerInstance.INSTANCE;
	}

	/**
	 * 启动后存储任务列表
	 */
	public final static Map<Integer, TaskListDto> currentHashMap = new ConcurrentHashMap<>();

	/**
	 * 工具类注入bean的方法
	 */
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@PostConstruct
	public void init() {
		MapContainerInstance.INSTANCE.threadPoolTaskScheduler = this.threadPoolTaskScheduler;
	}

	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return
	 */
	public TaskListDto getById(Integer id) {
		return currentHashMap.get(id);
	}

	/**
	 * 返回执行的任务列表
	 * 
	 * @return
	 */
	public Map<Integer, TaskListDto> getMapContainer() {
		return currentHashMap;
	}

	/**
	 * 将任务列表处理存入Map容器中
	 * 
	 * @param taskList
	 * @return
	 */
	public TaskListDto putMap(TaskList taskList) {
		TaskListDto taskListDto = new TaskListDto();
		taskListDto.setCron(taskList.getCron());
		String clazz = taskList.getClazz();
		Object obj = null;
		try {
			obj = Class.forName(clazz).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error("putMap时,反射实例化发生错误,错误原因-" + e.getMessage());
		}
		taskListDto.setTask(obj);
		log.info("ThreadPoolTaskScheduler {}", threadPoolTaskScheduler);
		ScheduledFuture<?> future = threadPoolTaskScheduler.schedule((Runnable) obj,
				new CronTrigger(taskList.getCron()));
		taskListDto.setFuture(future);
		currentHashMap.put(taskList.getId(), taskListDto);
		log.info("TaskList的实例存储 - " + taskList);
		return taskListDto;
	}

	/**
	 * 根据ID暂停任务
	 * 
	 * @param id
	 * @return
	 */
	public TaskListDto cancelMap(Integer id) {
		TaskListDto task = currentHashMap.get(id);
		ScheduledFuture<?> future = task.getFuture();
		if (future != null) {
			future.cancel(true);
		}
		return task;
	}
}
