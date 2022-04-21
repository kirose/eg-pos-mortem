package com.fimet.eglobal.rules;


import com.jayway.jsonpath.DocumentContext;

public class ValueOperator implements IStringOperator {
	private String jpath;
	public ValueOperator(String jpath) {
		super();
		this.jpath = jpath;
	}
	@Override
	public String eval(DocumentContext json) {
		if (jpath.startsWith("$.")) {
			return json.read(jpath);
		} else {
			return jpath;
		}
	}
	@Override
	public String toString() {
		return "%s";
	}
}
