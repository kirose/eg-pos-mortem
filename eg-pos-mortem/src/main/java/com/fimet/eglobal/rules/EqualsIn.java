package com.fimet.eglobal.rules;

import java.util.List;

import com.fimet.parser.IMappable;

public class EqualsIn implements IRule {
	private String address;
	private List<String> expected;
	public EqualsIn(String address, List<String> expected) {
		super();
		this.address = address;
		this.expected = expected;
	}
	@Override
	public boolean eval(IMappable mappable) {
		String value = mappable.get(address);
		if (expected == null) {
			return value == null;
		} else {
			return expected.contains(value);
		}
	}
	public String toString() {
		return String.format("Equals(%s,%s)",address, expected);
	}
}
