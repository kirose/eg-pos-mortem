package com.fimet.parser.mx;

import com.fimet.parser.AbstractMessageBitmapParser;
import com.fimet.parser.FormatException;
import com.fimet.parser.Message;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;

public class VisaParser extends AbstractMessageBitmapParser {
	public VisaParser(com.fimet.parser.IEParser entity) {
		super(entity);
	}

	@Override
	protected void formatHeader(IWriter writer, Message msg) {
		String header = (String)msg.getProperty(Message.HEADER);
		if (header == null || header.length() <= 0) {
			throw new FormatException("ISO header section invalid (length = 0)");
		}
		String mti = (String)msg.getProperty(Message.MTI);
		if (mti == null || mti.length() != 4) {
			throw new FormatException("MTI invalid expected length 4 found('"+(mti!=null?mti.length():0)+"'): '"+mti+"'");
		}
		int ln = (header.length()/2+1);
		String value = String.format("%02X", ln)+header+mti;
		writer.append(value.getBytes());
	}
	@Override
	protected void formatBitmap(IWriter writer, Message msg) {
		String bitmap = buildBitmap(msg);
		writer.append(Converter.binaryToHex(bitmap.getBytes()));
	}
	@Override
	protected void parseHeader(IReader reader, Message msg) {
		int lnHeader = Integer.parseInt(new String(reader.read(2)), 16);// 16010200EE0000000000000000000000000000000000
		String header = new String(reader.read((lnHeader-1)*2));
		/*String flagAndFormat = header.substring(0,2);
		String textFormat = header.substring(2,4);
		String totalMessageLength = header.substring(4,8);
		String destinationStationId = header.substring(4,8);
		String sourceStationId = header.substring(8,14);
		String roundTripControlInformation = header.substring(14,16);
		String baseIFlags = header.substring(16,20);
		String messageStatusFlags = header.substring(20,26);
		String batchNumber = header.substring(26,28);
		String reserved = header.substring(28,34);
		String userInformation = header.substring(34,36);*/
		msg.setProperty(Message.HEADER, header);
		msg.setProperty(Message.MTI, new String(reader.read(4)));
	}
}
