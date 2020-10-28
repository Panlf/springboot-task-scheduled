package com.plf.task.scheduled.job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRunnable implements Runnable {
	@Override
	public void run() {
		log.info("当前任务:{},当前线程:{}","任务1",Thread.currentThread().getName());
	}
}
