package com.fimet.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.Manager;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.converter.Converter;
/**
 * Una clase abstracta que genera un parseo general de ISO-8583 a un Message
 * Unicamente se parsean campos presentes en el Bitmap
 *    
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageBitmapParser extends AbstractMessageBitmapFormater {
	private static Logger logger = LoggerFactory.getLogger(AbstractMessageBitmapParser.class);
	protected static final boolean failOnErrorParseField = Manager.getPropertyBoolean("parser.failOnErrorParseField", false);
	public AbstractMessageBitmapParser(IEParser entity) {
		super(entity);
	}
	@Override
	public Message parseMessage(byte[] bytes) {
		Message msg = new Message();
		msg.setParser(this);
		IReader reader = new ByteBuffer(getConverter().convert(bytes));
		parseHeader(reader, msg);
		int[] bitmap = parseBitmap(reader);
		parseFields(reader, bitmap, msg);
		return msg;
	}
	protected void parseHeader(IReader reader, Message msg) {
		String headerValue = new String(reader.read(16));// ISO0234000700200
		msg.setProperty(Message.HEADER, headerValue.substring(0, 12));
		msg.setProperty(Message.MTI, headerValue.substring(12, 16));
	}
	protected int[] parseBitmap(IReader reader) {
		List<Integer> bitmap = new ArrayList<>();
		String bm;
		int noField = 2;
		do {
			bm = new String(Converter.hexToBinary(reader.read(16)));
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
	protected void parseFields(IReader reader, int[] bitmap, Message message) {
		Integer index = null;
		try {
			for (int id : bitmap) {
				getFieldGroup().parse(index = id, message, reader);
			}
		} catch (Exception e) {
			if (failOnErrorParseField) {
				throw e;
			} else {
				logger.warn(this+" error parsing field "+index,e);
			}
		}
	}
}
