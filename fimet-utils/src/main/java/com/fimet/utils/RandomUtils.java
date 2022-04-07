package com.fimet.utils;

import java.util.Random;

public final class RandomUtils {
	private RandomUtils() {
	}
	private static Random random = new Random();
	public static String randomAlphaNumeric(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlphaNumeric();
		}
		return new String(s);
	}
	public static String randomAlpha(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlpha();
		}
		return new String(s);
	}
	public static String randomAlphaLower(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlphaLower();
		}
		return new String(s);
	}
	public static String randomAlphaUpper(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlphaUpper();
		}
		return new String(s);
	}
	public static String randomAsciiVisible(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAsciiVisible();
		}
		return new String(s);
	}
	public static String randomNumeric(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomNumeric();
		}
		return new String(s);
	}
	public static char randomAlphaNumeric() {
		if (random.nextBoolean()) {
			return randomNumeric();
		} else {
			return randomAlpha();
		}
	}
	public static char randomAlphaLower() {
		return (char)(byte)(97+random.nextInt(26));
	}
	public static char randomAlphaUpper() {
		return (char)(byte)(65+random.nextInt(26));
	}
	public static char randomAlpha() {
		int b = random.nextInt(52);
		return (char)(byte)(b < 26 ? b+65 : b+71);
	}
	public static char randomNumeric() {
		return (char)(byte)(48+random.nextInt(10));
	}
	public static char randomAsciiVisible() {
		return (char)(byte)(32+random.nextInt(92));
	}
	public static void main(String[] args) {
		System.out.println(RandomUtils.randomAlphaLower(50));
		System.out.println(RandomUtils.randomAlphaUpper(50));
		System.out.println(RandomUtils.randomAlpha(50));
		System.out.println(RandomUtils.randomAlphaNumeric(50));
		System.out.println(RandomUtils.randomNumeric(50));
		System.out.println(RandomUtils.randomAsciiVisible(50));
	}
}
