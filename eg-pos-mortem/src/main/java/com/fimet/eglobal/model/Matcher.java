package com.fimet.eglobal.model;

public class Matcher {
	private String start;
	private String end;
	private String size;
	public Matcher(String start, String end, String size) {
		super();
		this.start = start;
		this.end = end;
		this.size = size;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
