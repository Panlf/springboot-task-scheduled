package com.plf.task.scheduled.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.container.MapContainer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CronService {

	@Autowired
	private TaskListService taskListService;

	public MapContainer mapContainer = MapContainer.getInstance();
	
	/**
	 * 添加定时任务列表，并启动
	 * @param taskList
	 */
	public void addTaskList(TaskList taskList){
		taskList.setCreatetime(new Date());
		taskList.setStatus(1);
		taskListService.save(taskList);
		log.info("CronService中的对象为 - "+mapContainer);
		mapContainer.putMap(taskList);
		log.info("执行新增操作,新增的实例为{},新增过后的容器中的任务{}",taskList,mapContainer.getMapContainer().toString());
	}
	
	/**
	 * 暂停定时任务
	 * @param id
	 */
	public void cancelTaskList(Integer id){
		TaskList taskList = getTaskListById(id);
		if(taskList==null){
			return;
		}
		//定义为暂停
		taskList.setStatus(2);
		taskListService.save(taskList);
		mapContainer.cancelMap(id);
	}
	
	/**
	 * 根据ID删除定时任务
	 * @param id
	 */
	public void deleteTaskList(Integer id){
		TaskList taskList = getTaskListById(id);
		if(taskList==null){
			return;
		}
		
		//定义为删除 
		taskList.setStatus(0);
		taskListService.save(taskList);
		
		mapContainer.deleteMap(id);
		
		log.info("执行删除操作,删除的ID为{},删除的实例为{},删除过后的容器中的任务{}",id,taskList,mapContainer.getMapContainer().toString());
	}
	
	/**
	 * 重启启动定时任务
	 * @param id
	 */
	public void restartTaskList(Integer id){
		TaskList taskList = getTaskListById(id);
		if(taskList==null){
			return;
		}
		//定义为启动 
		taskList.setStatus(1);
		taskListService.save(taskList);
		mapContainer.restartMap(taskList);
		log.info("执行启动操作,启动的ID为{},启动的实例为{},启动过后的容器中的任务{}",id,taskList,mapContainer.getMapContainer().toString());
	}
	
	/**
	 * 更新定时任务
	 * @param id
	 * @param cron
	 */
	public void updateTaskList(Integer id,String cron) {
		
		TaskList taskList = getTaskListById(id);
		taskList.setCron(cron);
		
		//暂停当前id的任务
		mapContainer.cancelMap(id);
		
		//将任务重置进容器中
		mapContainer.putMap(taskList);
		
		//更新
		taskListService.updateProxyCronById(cron, id);
		
		log.info("执行修改操作,修改的ID为{},修改的实例为{},修改过后的容器中的任务{}",id,taskList,mapContainer.getMapContainer().toString());
	}
	
	
	/**
	 * 获取所有的某包下所有的类名
	 * @param packageName
	 * @return
	 */
	public List<Class<?>> getJobClass(String packageName) {
		List<Class<?>> list = new ArrayList<>();
		try {
			Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader()
					.getResources(packageName.replace(".", "/"));
			URL url = null;
			File file = null;
			File[] files = null;
			Class<?> clazz = null;
			String className = null;
			while (iterator.hasMoreElements()) {
				url = iterator.nextElement();
				if ("file".equals(url.getProtocol())) {
					file = new File(url.getPath());
					if (file.isDirectory()) {
						files = file.listFiles();
						for (File f : files) {
							className = f.getName();
							className = className.substring(0, className.lastIndexOf("."));
							clazz = Thread.currentThread().getContextClassLoader()
									.loadClass(packageName + "." + className);
							list.add(clazz);
						}
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据ID获取任务表
	 * 
	 * @param id
	 * @return
	 */
	public TaskList getTaskListById(Integer id) {
		Optional<TaskList> optTask = taskListService.findById(id);
		if (optTask.isPresent()) {
			return optTask.get();
		}
		return null;
	}

	/**
	 * 根据任务名称分页查找
	 * @param pageSize
	 * @param pageNumber
	 * @param taskname
	 * @return
	 */
	public Page<TaskList> findPageByName(Integer pageSize, Integer pageNumber, String taskname) {
		Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
		Specification<TaskList> spec = new Specification<TaskList>() {
			
			private static final long serialVersionUID = -1688480239483372044L;

			@Override
			public Predicate toPredicate(Root<TaskList> root, CriteriaQuery<?> criteria,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				if (taskname != null && taskname.trim().length() > 0) {
					list.add(criteriaBuilder.like(root.get("taskname").as(String.class), "%" + taskname + "%"));
				}
				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};

		return taskListService.findAll(spec, pageable);
	}
}
