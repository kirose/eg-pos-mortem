package com.fimet.eglobal.classification;

import com.jayway.jsonpath.DocumentContext;

public class NotExists implements IRule {
	private String jpath;
	public NotExists(String jpath) {
		super();
		this.jpath = jpath;
	}
	@Override
	public boolean eval(DocumentContext json) {
		String value = json.read(jpath);
		return value==null;
	}
	public String toString() {
		return String.format("not exists(%s)",jpath);
	}
}
