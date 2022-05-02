package com.fimet.eglobal.rules;


import com.jayway.jsonpath.DocumentContext;

public class ValueOperator implements IValueOperator {
	private String jpath;
	public ValueOperator(String jpath) {
		super();
		this.jpath = jpath;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T>T eval(DocumentContext json) {
		if (jpath.startsWith("$.")) {
			return json.read(jpath);
		} else {
			return (T)jpath;
		}
	}
	@Override
	public String toString() {
		return "%s";
	}
}
