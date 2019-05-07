package com.plf.task.scheduled.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务表
 * @author plf 2019年5月5日上午11:19:57
 *
 */
@Entity
@Data
@ApiModel("定时任务")
public class TaskList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty("主键ID-数据库自增")
	private Integer id;
	
	/**
	 * 任务表达式
	 */
	@Column
	@ApiModelProperty("任务表达式")
	private String cron;
	
	/**
	 * 任务全类名
	 */
	@Column
	@ApiModelProperty("任务全类名")
	private String clazz;
	
	/**
	 * 状态 
	 * 0 代表 删除
	 * 1 代表 启动
	 * 2 代表 停止
	 */
	@Column
	@ApiModelProperty("任务状态 0 删除 1启动 2 停止")
	private Integer status;
	
	/**
	 * 任务名
	 */
	@Column
	@ApiModelProperty("任务名")
	private String taskname;
	
	/**
	 * 创建时间
	 */
	@Column
	@ApiModelProperty("创建时间")
	private Date createtime;
	
}
