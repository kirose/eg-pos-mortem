package com.fimet.eglobal.classification;

import java.util.List;

import com.jayway.jsonpath.DocumentContext;

public class EqualsIn implements IRule {
	private String address;
	private List<String> expected;
	public EqualsIn(String address, List<String> expected) {
		super();
		this.address = address;
		this.expected = expected;
	}
	@Override
	public boolean eval(DocumentContext json) {
		String value = json.read(address);
		if (expected == null) {
			return value == null;
		} else {
			return expected.contains(value);
		}
	}
	public String toString() {
		return String.format("equals(%s,%s)",address, expected);
	}
}
