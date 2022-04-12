package com.fimet.eglobal.classification;

import com.jayway.jsonpath.DocumentContext;

public class Equals implements IRule {
	private String jpath;
	private String expected;
	public Equals(String jpath, String expected) {
		super();
		this.jpath = jpath;
		this.expected = expected;
	}
	@Override
	public boolean eval(DocumentContext json) {
		String value = json.read(jpath);
		if (value == null) {
			return expected == null;
		} else {
			return value.equals(expected);
		}
	}
	public String toString() {
		return String.format("equals(%s,%s)",jpath, expected);
	}
}
