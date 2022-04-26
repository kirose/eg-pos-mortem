package com.fimet.parser;

import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.utils.ByteBuilder;
import com.fimet.utils.IWriter;
/**
 * 
 * @author Marco A. Salazar
 *
 */
public abstract class AbstractMessageFormater extends BaseMessageParser {
	private static Logger logger = LoggerFactory.getLogger(AbstractMessageFormater.class);
	public AbstractMessageFormater(IEParser entity) {
		super(entity);
	}

	@Override
	public byte[] formatMessage(IMessage msg) {
		IWriter writer = new ByteBuilder();
		formatFields(writer, msg);
		byte[] iso = getConverter().deconvert(getConverter().deconvert(writer.getBytes()));
		return iso;
	}
	protected void formatFields(IWriter writer, IMessage msg) {
		List<IFieldParser> roots = getFieldGroup().getRoots();
		for (IFieldParser parser : roots) {
			try {
				parser.format(writer, msg);
			} catch (Exception e) {
				logger.error(this+" error formating field "+parser.getIdField(),e);
				throw e;
			}
		}
	}
}
