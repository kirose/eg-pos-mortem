package com.fimet.eglobal.rules;

import com.fimet.parser.IMappable;

public class Exists implements IRule {
	private String address;
	public Exists(String address) {
		super();
		this.address = address;
	}
	@Override
	public boolean eval(IMappable mappable) {
		String value = mappable.get(address);
		return value!=null;
	}
	public String toString() {
		return String.format("Exists(%s)",address);
	}
}
