package com.fimet.eglobal.reports;

public class ReportResponse {
	private String name;
	private String startExecution;
	private String endExecution;
	public ReportResponse(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
