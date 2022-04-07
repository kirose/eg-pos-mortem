package com.fimet.utils;

public class ClassModel {
	private String className;
	private String source;
	public ClassModel(String className, String source) {
		super();
		this.className = className;
		this.source = source;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
