package com.fimet.parser.adapter;

import com.fimet.parser.AdapterException;
import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStringAdapter;
import com.fimet.utils.ByteBuilder;
import com.fimet.utils.converter.Converter;

public class LogHexParserAdapter extends Adapter implements IStringAdapter, IByteArrayAdapter {

	//public static final Pattern PATTERN_LOG_MSG = Pattern.compile("([\t ]*[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[\t ]+[0-9A-Fa-f]{2}[ \n\r\t]*)");
	
	public LogHexParserAdapter(int id, String name) {
		super(id, name);
	}

	@Override
	public boolean isAdaptable(String message) {
		return message != null && message.matches(REGEXP_LOG) && message.matches("(?s).*([A-Fa-f][0-9]|[A-Fa-f0-9]{2}|[0-9][A-Fa-f])(?s).*");
	}
	@Override
	public byte[] readString(String message) {
		if (isAdaptable(message)) {
			//Matcher m = PATTERN_LOG_MSG.matcher(message);
			//if (m.find()) {
				StringBuilder sb = new StringBuilder();
				
				int ln = message.length();
				char c;
				for (int i = 0; i < ln; i++) {
					c = message.charAt(i);
					if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
						sb.append(c);
					}
				}
				return Converter.hexToAscii(sb.toString().getBytes());
			//} else {
				//throw new AdapterException("Cannot adapt the message as log hex");
			//}
		} else {
			throw new AdapterException("The message is not a log hex message");
		}
	}

	@Override
	public String writeString(byte[] message) {
		ByteBuilder writer = new ByteBuilder();
		byte[] hex;
		byte b;
		for (int i = 0; i < message.length;) {
			b = message[i];
			hex = Converter.asciiToHex(b);
			writer.append(hex[0]);
			writer.append(hex[1]);
			i++;
			if (i % 16 == 0) {// '\n'
				writer.append((byte)10);
			} else if (i % 4 == 0) {// '  '
				writer.append((byte)32);
				writer.append((byte)32);
			} else {// ' '
				writer.append((byte)32);
			}
		}
		return new String(writer.getBytes());
	}

	@Override
	public boolean isAdaptable(byte[] message) {
		return isAdaptable(new String(message));
	}

	@Override
	public byte[] readByteArray(byte[] message) {
		return readString(new String(message));
	}

	@Override
	public byte[] writeByteArray(byte[] message) {
		return writeString(message).getBytes();
	}
}
