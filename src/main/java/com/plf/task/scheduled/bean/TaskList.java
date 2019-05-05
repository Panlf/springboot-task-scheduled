package com.plf.task.scheduled.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 任务表
 * @author plf 2019年5月5日上午11:19:57
 *
 */
@Entity
@Data
public class TaskList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * 任务表达式
	 */
	@Column
	private String cron;
	
	/**
	 * 任务全类名
	 */
	@Column
	private String clazz;
	
	/**
	 * 状态 
	 * 0 代表 删除
	 * 1 代表 启动
	 * 2 代表 停止
	 */
	@Column
	private Integer status;
	
	/**
	 * 任务名
	 */
	@Column
	private String taskname;
	
	/**
	 * 创建时间
	 */
	@Column
	private Date createtime;
	
}
