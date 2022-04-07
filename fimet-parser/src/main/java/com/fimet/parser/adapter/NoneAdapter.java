package com.fimet.parser.adapter;

import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStringAdapter;

public class NoneAdapter extends Adapter implements IStringAdapter, IByteArrayAdapter {

	
	public NoneAdapter(int id, String name) {
		super(id, name);
	}

	@Override
	public boolean isAdaptable(byte[] message) {
		return true;
	}

	@Override
	public boolean isAdaptable(String message) {
		return true;
	}

	@Override
	public byte[] readString(String message) {
		return message == null ? null : message.getBytes();
	}

	@Override
	public String writeString(byte[] message) {
		return new String(message);
	}

	@Override
	public byte[] readByteArray(byte[] message) {
		return readString(new String(message));
	}

	@Override
	public byte[] writeByteArray(byte[] message) {
		return message;
	}

}
