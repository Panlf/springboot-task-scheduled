package com.plf.task.scheduled.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.plf.task.scheduled.bean.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList,Integer>,JpaSpecificationExecutor<TaskList> {

	public List<TaskList> findByStatus(Integer status);
} 
