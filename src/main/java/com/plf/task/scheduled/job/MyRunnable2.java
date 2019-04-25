package com.plf.task.scheduled.job;

import java.util.Date;

public class MyRunnable2 implements Runnable {
	@Override
	public void run() {
		System.out.println("任务二----" + new Date());
	}
}
