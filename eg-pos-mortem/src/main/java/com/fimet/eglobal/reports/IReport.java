package com.fimet.eglobal.reports;

import java.io.Closeable;

public interface IReport extends Closeable {
	public void add(String jsonMatch, String jsonValidate);
	public void close();
	public String getName();
}
