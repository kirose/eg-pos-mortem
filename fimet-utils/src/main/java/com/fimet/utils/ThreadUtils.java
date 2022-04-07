package com.fimet.utils;

import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;


public final class ThreadUtils {
	public static ThreadUtils instance;
	public static final int THREAD_POOL_SIZE = 10;
	private ExecutorService executor;
	/*private static ThreadUtils getInstance() {
		if (instance == null) {
			instance = new ThreadUtils();
		}
		return instance;
	}*/
	
	public ThreadUtils() {
		//executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}
	public ExecutorService getExecutor() {
		return executor;
	}
	public static void runAsync(Runnable run) {
		new Thread(run).start();
		//PlatformUI.getWorkbench().getDisplay().asyncExec(run);
	}
//	public static void runOnMainThread(Runnable run) {
//		PlatformUI.getWorkbench().getDisplay().syncExec(run);
//	}
}
