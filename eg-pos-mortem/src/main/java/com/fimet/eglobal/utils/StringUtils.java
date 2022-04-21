package com.fimet.eglobal.utils;

public class StringUtils {
	public static String prettyJPath(String key) {
		if (key == null)
			return key;
		if (key.startsWith("$."))
			return key.substring(2);
		return key;
	}
}
