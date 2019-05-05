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
import com.plf.task.scheduled.repository.TaskListRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CronService {

	@Autowired
	private TaskListRepository taskListRepository;

	public MapContainer mapContainer = MapContainer.getInstance();
	
	/**
	 * 添加定时任务列表，并启动
	 * @param taskList
	 */
	public void addTaskList(TaskList taskList){
		taskList.setCreatetime(new Date());
		taskList.setStatus(1);
		taskListRepository.save(taskList);
		log.info("CronService中的对象为 - "+mapContainer);
		mapContainer.putMap(taskList);
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
		taskList.setStatus(2);
		taskListRepository.save(taskList);
		mapContainer.cancelMap(id);
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
		Optional<TaskList> optTask = taskListRepository.findById(id);
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
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
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

		return taskListRepository.findAll(spec, pageable);
	}
}
