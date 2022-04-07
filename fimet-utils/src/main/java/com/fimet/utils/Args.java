package com.fimet.utils;


public final class Args {
	private Args(){}
	public static void notNull(String name, Object value) {
		if (value==null)
			throw new NullPointerException("Cannot be null "+name);
	}
	public static void isIpAddress(String name, String address) {
		notNull(name, address);
		if (!address.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}"))
			throw new IllegalArgumentException("Invalid " + name);
	}
	public static void isPositive(String name, Integer value) {
		notNull(name, value);
		if (value<0)
			throw new IllegalArgumentException("Cannot be negative "+name);
	}
	public static void notBlank(String name, String value) {
		notNull(name, value);
		if (StringUtils.isBlank(value))
			throw new IllegalArgumentException("Cannot be blank "+name);
	}
	public static void equals(String name, Object value, Object expected) {
		if (value == expected)
			return;
		if (value == null && expected!=null) {
			throw new IllegalArgumentException("Expected "+name+" be equals to "+expected);
		}
		if (!value.equals(expected)) {
			throw new IllegalArgumentException("Expected "+name+" be equals to "+expected);
		}
	}
}
