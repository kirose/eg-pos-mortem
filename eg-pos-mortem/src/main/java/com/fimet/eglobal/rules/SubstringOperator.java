package com.fimet.eglobal.rules;


import com.jayway.jsonpath.DocumentContext;

public class SubstringOperator implements IValueOperator {
	private String jpath;
	private int start;
	private int end;
	public SubstringOperator(String jpath, int start, int end) {
		super();
		this.jpath = jpath;
		this.start = start;
		this.end = end;
	}
	public SubstringOperator(String jpath, int start) {
		this(jpath, start, -1);
	}
	@SuppressWarnings("unchecked")
	@Override
	public String eval(DocumentContext json) {
		String value = json.read(jpath);
		if (value == null) {
			return null;
		}
		if (end == -1) {
			return value.substring(start);
		} else {
			return value.substring(start, end);
		}
	}
	@Override
	public String toString() {
		return "substring(%s," +start+","+end+")";
	}
}
