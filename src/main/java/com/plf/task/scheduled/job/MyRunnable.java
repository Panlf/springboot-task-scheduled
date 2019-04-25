package com.plf.task.scheduled.job;

import java.util.Date;

public class MyRunnable implements Runnable {
	@Override
	public void run() {
		System.out.println("任务一----" + new Date());
	}
}
