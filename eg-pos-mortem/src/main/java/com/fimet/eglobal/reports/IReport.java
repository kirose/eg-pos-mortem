package com.fimet.eglobal.reports;

import java.io.Closeable;

import com.jayway.jsonpath.DocumentContext;

public interface IReport extends Closeable {
	public void add(DocumentContext mtch);
	public void close();
	public String getName();
}
