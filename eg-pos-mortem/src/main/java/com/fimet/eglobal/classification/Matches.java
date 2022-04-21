package com.fimet.eglobal.classification;

import com.jayway.jsonpath.DocumentContext;
import static com.fimet.eglobal.utils.StringUtils.prettyJPath;

public class Matches implements IRule {
	private String jpath;
	private String expected;
	public Matches(String jpath, String expected) {
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
			return value.matches(expected);
		}
	}
	public String toString() {
		return String.format("matches(%s,%s)",prettyJPath(jpath),expected);
	}
}
