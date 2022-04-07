package com.fimet;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadManager extends AbstractManager implements IThreadManager {
	volatile ThreadPoolExecutor executor;
	@Autowired private IPropertiesManager properties;
	public ThreadManager() {
	}
	@PostConstruct
	public void start() {
		Integer numOfThreads = properties.getInteger("thread.numOfThreads", 2);
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
		//executor.setMaximumPoolSize(10);
	}
	@Override
	public void execute(Runnable runnable) {
		executor.execute(runnable);
		//if (executor.get)
		//System.out.println("ThreadPoolQueueSize-"+executor.getCompletedTaskCount()+"/"+executor.getActiveCount()+"/"+executor.getTaskCount());
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
