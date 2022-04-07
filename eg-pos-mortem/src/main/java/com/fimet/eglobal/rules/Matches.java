package com.fimet.eglobal.rules;

import com.fimet.parser.IMappable;

public class Matches implements IRule {
	private String address;
	private String expected;
	public Matches(String address, String expected) {
		super();
		this.address = address;
		this.expected = expected;
	}
	@Override
	public boolean eval(IMappable mappable) {
		String value = mappable.get(address);
		if (value == null) {
			return expected == null;
		} else {
			return value.matches(expected);
		}
	}
	public String toString() {
		return String.format("Matches(%s,%s)",address, expected);
	}
}
