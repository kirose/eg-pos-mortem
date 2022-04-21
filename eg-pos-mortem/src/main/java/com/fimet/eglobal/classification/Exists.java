package com.fimet.eglobal.classification;

import com.jayway.jsonpath.DocumentContext;
import static com.fimet.eglobal.utils.StringUtils.prettyJPath;

public class Exists implements IRule {
	private String jpath;
	public Exists(String jpath) {
		super();
		this.jpath = jpath;
	}
	@Override
	public boolean eval(DocumentContext json) {
		String value = json.read(jpath);
		return value!=null;
	}
	public String toString() {
		return String.format("existe(%s)",prettyJPath(jpath));
	}
}
