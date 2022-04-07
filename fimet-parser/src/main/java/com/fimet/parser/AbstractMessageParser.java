package com.fimet.parser;

import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
/**
 * Una clase abstracta que genera un parseo general de un mensaje ISO-8583 a un Message
 * Unicamente se parsean campos presentes en el Bitmap
 *    
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageParser extends AbstractMessageFormater {
	private static Logger logger = LoggerFactory.getLogger(AbstractMessageParser.class);
	public AbstractMessageParser(IEParser entity) {
		super(entity);
	}
	@Override
	public Message parseMessage(byte[] bytes) {
		Message msg = new Message();
		msg.setParser(this);
		IReader reader = new ByteBuffer(getConverter().convert(bytes));
		parseFields(reader, msg);
		return msg;
	}
	protected void parseFields(IReader reader, Message message) {
		List<IFieldParser> roots = getFieldGroup().getRoots();
		IFieldParser last = null;
		try {
			for (IFieldParser parser : roots) {
				last = parser;
				parser.parse(reader, message);
			}
		} catch (Exception e) {
			if (getFailOnError()) {
				throw e;
			} else {
				logger.warn("Error parsing "+last,e);
			}
		}
	}
	private boolean getFailOnError() {
		return false;//PreferenceDAO.getBoolean(PreferenceDAO.FAIL_ON_PARSE_FIELD_ERROR, Parser.DEFAUT_FAIL_ON_ERROR);
	}
}
