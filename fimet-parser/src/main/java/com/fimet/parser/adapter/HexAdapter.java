package com.fimet.parser.adapter;

import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStringAdapter;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.converter.Converter;

public class HexAdapter extends Adapter implements IStringAdapter, IByteArrayAdapter {

	public HexAdapter(int id, String name) {
		super(id, name);
	}

	@Override
	public boolean isAdaptable(byte[] message) {
		for (byte b : message) {
			if (!ByteUtils.isHex(b)) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isAdaptable(String message) {
		return message.matches("^[0-9A-fa-f]$") && message.matches("[0-9]+") && message.matches("[A-Fa-f]+");
	}

	@Override
	public byte[] readString(String message) {
		return message == null ? null : Converter.hexToAscii(message.getBytes());
	}

	@Override
	public String writeString(byte[] message) {
		return message == null ? null : new String(Converter.asciiToHex(message));
	}

	@Override
	public byte[] readByteArray(byte[] message) {
		return Converter.hexToAscii(message);
	}

	@Override
	public byte[] writeByteArray(byte[] message) {
		return Converter.asciiToHex(message);
	}


}
