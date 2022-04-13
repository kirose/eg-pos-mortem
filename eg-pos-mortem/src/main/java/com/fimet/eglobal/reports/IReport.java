package com.fimet.eglobal.reports;

public interface IReport {
	public void add(String jsonMatch, String jsonValidate);
	public void close();
	public String getName();
}
