package com.fimet.eglobal.store;

public class Index {
	public static final int INDEX_SIZE = 8+8+4;
	long key;
	long offset;
	int length;
	public long getKey() {
		return key;
	}
	public long getOffset() {
		return offset;
	}
	public int getLength() {
		return length;
	}
	@Override
	public String toString() {
		return "Index [key=" + key + ", offset=" + offset + ", length=" + length + "]";
	}
}
