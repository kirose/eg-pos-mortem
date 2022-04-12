package com.fimet.eglobal.matcher;

public class MatcherResponse {
	private String id;
	private String startExecution;
	private String endExecution;
	public MatcherResponse(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartExecution() {
		return startExecution;
	}
	public void setStartExecution(String startExecution) {
		this.startExecution = startExecution;
	}
	public String getEndExecution() {
		return endExecution;
	}
	public void setEndExecution(String endExecution) {
		this.endExecution = endExecution;
	}
}
