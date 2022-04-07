package com.fimet.parser;


import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.Manager;
import com.fimet.utils.ByteBuilder;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;
/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageBitmapFormater extends BaseMessageParser {
	private static Logger logger = LoggerFactory.getLogger(AbstractMessageBitmapFormater.class);
	protected static final boolean failOnErrorFormatField = Manager.getPropertyBoolean("parser.failOnErrorFormatField", false);
	private static final int SIZE_BITMAP = 4*16;
	private static final String EMPTY_BITMAP = "1000000000000000000000000000000000000000000000000000000000000000";
	
	public AbstractMessageBitmapFormater(IEParser entity) {
		super(entity);
	}

	@Override
	public byte[] formatMessage(IMessage message) {
		IWriter writer = new ByteBuilder();
		Message msg = (Message)message;
		formatHeader(writer, msg);
		formatBitmap(writer, msg);
		formatFields(writer, msg);
		byte[] iso = getConverter().deconvert(writer.getBytes());
		return iso;
	}
	protected void formatHeader(IWriter writer, Message msg) {
		String header = (String)msg.getProperty(Message.HEADER);
		if (header == null || header.length() != 12) {
			throw new FormatException("ISO header section invalid expected length 12 found("+(header!=null?header.length():0)+"): '"+header+"'");
		}
		String mti = (String)msg.getProperty(Message.MTI);
		if (mti == null || mti.length() != 4) {
			throw new FormatException("MTI invalid expected length 4 found('"+(mti != null?mti.length():0)+"'): '"+mti+"'");
		}
		writer.append(header);
		writer.append(mti);
	}
	protected void formatBitmap(IWriter writer, Message msg) {
		String bitmap = buildBitmap(msg);
		writer.append(Converter.binaryToHex(bitmap.getBytes()));
	}
	protected String buildBitmap(Message msg) {
		int[] bitmap = msg.getBitmap();
	
		int endBitmap = bitmap == null || bitmap.length == 0 ? 1 : bitmap(bitmap[bitmap.length-1]);
		StringBuilder bitmapStr = new StringBuilder();
		for (int i = 0; i < endBitmap; i++) {
			bitmapStr.append(EMPTY_BITMAP);
		}
		bitmapStr.replace(bitmapStr.length()-64, bitmapStr.length()-63, "0");
		int bit;
		for (int index : bitmap) {
			bit = bitmap(index);
			index = index + (bit == 1 ? -1 : bit - 3);
			bitmapStr.replace(index, index+1, "1");
		}
		return bitmapStr.toString();
	}
	/**
	 * Returns the bitmap number to which index belongs
	 * @param index
	 * @return bitmap number
	 */
	protected int bitmap(int index) {
		if (index <= SIZE_BITMAP) {
			return 1;
		} else {
			return 2;
		}
		//return ((index - index%SIZE_BITMAP)/SIZE_BITMAP+1);
	}
	protected void formatFields(IWriter writer, Message msg) {
		for (int field : msg.getBitmap()) {
			try {
				getFieldGroup().format(field, msg, writer);
			} catch (Exception e) {
				if (failOnErrorFormatField) { 
					throw e;
				} else {
					logger.error(this+" error formating field "+field,e);
				}
			}
		}
	}
}
