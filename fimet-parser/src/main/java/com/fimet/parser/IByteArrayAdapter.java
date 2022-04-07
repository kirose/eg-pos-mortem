package com.fimet.parser;

public interface IByteArrayAdapter extends IAdapter {
	boolean isAdaptable(byte[] message);
	byte[] readByteArray(byte[] message);
	byte[] writeByteArray(byte[] message);
}
