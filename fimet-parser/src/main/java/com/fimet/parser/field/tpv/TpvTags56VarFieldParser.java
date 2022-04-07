package com.fimet.parser.field.tpv;



import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class TpvTags56VarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(TpvTags56VarFieldParser.class);
	public TpvTags56VarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null && message.hasField(idField)) {
			try {
				IReader reader = new ByteBuffer(message.getValueAsBytes(idField));
				if (!reader.hasNext()) {
					return;
				}
				parseTags(message, reader);
			} catch (Exception e) {
				if (failOnErrorParseField) {
					throw e;
				} else {
					logger.warn(this+" error parsing tokens "+idField,e);
				}
			}
		}
	}
	private void parseTags(IMessage message, IReader reader) {
		while (reader.hasNext()) {
			parseTag(reader, message);
		}
	}
	private byte[] parseTag(IReader reader, IMessage message) {
		String nextTag = new String(reader.peek(2));
		return group.parse(idField+"."+nextTag, message, reader);
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		for (String idChild : message.getIdChildren(idField)) {
			group.format(idChild, message, writer);
		}
	}
}
