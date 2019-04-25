package com.plf.task.scheduled.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.plf.task.scheduled.bean.TaskBean;
import com.plf.task.scheduled.bean.TaskTableBean;

@Service
public class CronService {

	public final static Map<Integer,TaskBean> currentHashMap = new ConcurrentHashMap<>();
	
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	
	public TaskBean getCronById(Integer id){
		TaskTableBean bean = getDataById(id);
		TaskBean task=getIncorrentCron(bean);
		return task;
	}
	
	
	private TaskBean getIncorrentCron(TaskTableBean bean){
		TaskBean task =new TaskBean();
		task.setCron(bean.getCron());
		String str = bean.getClazz();
		Object obj=null;
		try {
			obj=Class.forName(str).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		task.setTask(obj);
		ScheduledFuture<?> future = threadPoolTaskScheduler.schedule((Runnable) obj, new CronTrigger(bean.getCron()));
		task.setFuture(future);
		currentHashMap.put(bean.getId(), task);
		return task;
	}
	
	public void stopTaskBean(Integer id){
		TaskBean task = currentHashMap.get(id);
		ScheduledFuture<?> future = task.getFuture();
		if(future != null){
			future.cancel(true);
		}
	}
	
	public List<Class<?>> getJobClass(String packageName){
		List<Class<?>> list = new ArrayList<>();
		try {
			Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
			URL url = null;
			File file = null;
			File[] files = null;
			Class<?> clazz = null;
			String className=null;
			while(iterator.hasMoreElements()){
				url = iterator.nextElement();
				if("file".equals(url.getProtocol())){
					file = new File(url.getPath());
					if(file.isDirectory()){
						files = file.listFiles();
						for (File f : files) {
							className = f.getName();
							className = className.substring(0,className.lastIndexOf("."));
							clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className);
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
	 * 模拟数据库
	 * @param id
	 * @return
	 */
	private TaskTableBean getDataById(Integer id){
		TaskTableBean bean1 = new TaskTableBean(1,"0/2 * * * * *","com.plf.tssk.scheduled.job.MyRunnable");
		TaskTableBean bean2 = new TaskTableBean(2,"0/5 * * * * *","com.plf.tssk.scheduled.job.MyRunnable2");
		if(id==1){
			return bean1;
		}else{
			return bean2;
		}
	}
}
