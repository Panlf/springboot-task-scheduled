package com.plf.task.scheduled.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.plf.task.scheduled.bean.TaskList;
import com.plf.task.scheduled.repository.TaskListRepository;

@Service
public class TaskListService {
	
	@Autowired
	private TaskListRepository taskListRepository;

	@Transactional
	public void save(TaskList taskList) {
		taskListRepository.save(taskList);
	}

	/**
	 * 按照创建时间倒叙输出全部任务
	 * @return
	 */
	public List<TaskList> findAll(){
		return taskListRepository.findAll(new Sort(Sort.Direction.DESC,"createtime"));
	}

	public void updateProxyCronById(String cron, Integer id){
		((TaskListService)AopContext.currentProxy()).updateCronById(cron, id);
	}

	public Page<TaskList> findAll(Specification<TaskList> spec, Pageable pageable) {
		return taskListRepository.findAll(spec,pageable);
	}

	public Optional<TaskList> findById(Integer id) {
		return taskListRepository.findById(id);
	}

	@Transactional
	@Async
	public void updateCronById(String cron, Integer id) {
		taskListRepository.updateCronById(cron, id);
	}
}
