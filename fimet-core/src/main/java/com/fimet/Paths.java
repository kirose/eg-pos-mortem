package com.fimet;

import java.io.File;

public class Paths {
	public static File FIMET = new File(System.getProperty("user.dir"));
	public static File STORE = new File("store");
	public static File TASKS = new File("tasks");
	public static File MODEL = new File("model");
	public static File BIN = new File("bin");
	public static File SRC = new File("src");
	public static File LIB = new File("lib");
	public static File LOGS = new File("logs");
	public static File TMP = new File("tmp");
	public static File USECASES = new File("usecases");
	public static File STRESS = new File("stress");
	
//	static {
//		create(FIMET);
//		create(STORE);
//		create(TASKS);
//		create(MODEL);
//		create(BIN);
//		create(SRC);
//		create(LIB);
//		create(LOGS);
//		create(TMP);
//		create(USECASES);
//		create(STRESS);
//	}
//	private static void create(File path) {
//		if (!path.exists()) {
//			path.mkdirs();
//		}
//	}
}
