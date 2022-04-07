package com.fimet.parser.mx;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fimet.parser.AbstractMessageBitmapParser;
import com.fimet.parser.FormatException;
import com.fimet.parser.Message;
import com.fimet.utils.ByteBuilder;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.StringUtils;
import com.fimet.utils.converter.Converter;

public class AmexParser extends AbstractMessageBitmapParser {
	public AmexParser(com.fimet.parser.IEParser entity) {
		super(entity);
	}
	@Override
	protected void formatHeader(IWriter writer, Message msg) {
		String mti = (String)msg.getProperty(Message.MTI);
		if (mti == null || mti.length() != 4) {
			throw new FormatException("MTI invalid expected length 4 found('"+(mti != null?mti.length():0)+"'): '"+mti+"'");
		}
		writer.append(Converter.asciiToEbcdic(mti.getBytes()));
	}
	@Override
	protected void formatBitmap(IWriter writer, Message msg) {
		String bitmap = buildBitmap(msg);
		String bitmapHex = StringUtils.leftPad(new BigInteger(bitmap,2).toString(16).toUpperCase(),bitmap.length()/4,'0');
		writer.append(Converter.hexToAscii(bitmapHex.getBytes()));
	}
	@Override
	protected void parseHeader(IReader reader, Message msg) {
		String mti = new String(Converter.ebcdicToAscii(reader.read(4)));
		msg.setProperty(Message.HEADER, "");
		msg.setProperty(Message.MTI, mti);
	}
	@Override
	protected int[] parseBitmap(IReader reader) {
		List<Integer> bitmap = new ArrayList<>();
		String bm;
		int noField = 2;
		do {
			bm = new String(Converter.asciiToBinary(reader.read(8)));
			for (int i = 1; i < bm.length(); i++) {
				if (bm.charAt(i) == '1') {
					bitmap.add(noField);
				}
				noField++;
			}
			noField++;
		} while (bm.charAt(0) == '1');
		int[] bitmapArray = new int[bitmap.size()];
		int i = 0;
		for (Integer id : bitmap) {
			bitmapArray[i++] = id;
		}
		return bitmapArray;
	}
	@Override
	protected void parseFields(IReader reader, int[] bitmap, Message message) {
		reader.convert(Converter.EBCDIC_TO_ASCII);
		super.parseFields(reader, bitmap, message);
	}
	@Override
	protected void formatFields(IWriter writer, Message msg) {
		IWriter tmp = new ByteBuilder();
		super.formatFields(tmp, msg);
		writer.append(Converter.ASCII_TO_EBCDIC.convert(tmp.getBytes()));
	}
}