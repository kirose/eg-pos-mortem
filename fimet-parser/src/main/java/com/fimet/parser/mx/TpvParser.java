package com.fimet.parser.mx;

import com.fimet.parser.AbstractMessageBitmapParser;
import com.fimet.parser.FormatException;
import com.fimet.parser.Message;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class TpvParser extends AbstractMessageBitmapParser{
	public TpvParser(com.fimet.parser.IEParser entity) {
		super(entity);
	}
	@Override
	protected void formatHeader(IWriter writer, Message msg) {
		String header = (String)msg.getProperty(Message.HEADER);
		if (header == null || header.length() <= 0) {
			throw new FormatException("ISO header section invalid (length = 0)");
		}
		if (header.length() != 10) {
			throw new FormatException("ISO header section invalid (length = "+(header!=null?header.length():0)+") expected length = 10");
		}
		String mti = (String)msg.getProperty(Message.MTI);
		if (mti == null || mti.length() != 4) {
			throw new FormatException("MTI invalid expected length 4 found('"+(mti!=null?mti.length():0)+"'): '"+mti+"'");
		}
		String value = header+mti;
		writer.append(value.getBytes());
	}
	@Override
	protected void parseHeader(IReader reader, Message msg) {
		String headerValue = new String(reader.read(14));
		msg.setProperty(Message.HEADER, headerValue.substring(0, 10));
		msg.setProperty(Message.MTI, headerValue.substring(10, 14));
	}
}
