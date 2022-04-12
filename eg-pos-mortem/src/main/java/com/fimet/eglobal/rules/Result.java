package com.fimet.eglobal.rules;

public class Result {
	private String[] arguments;
	private boolean value;
	public Result(boolean result, String[] args) {
		this.value = result;
		this.arguments = args;
	}
	public Result(boolean result) {
		this.value = result;
	}
	public String[] getArguments() {
		return arguments;
	}
	public boolean getValue() {
		return value;
	}
}
