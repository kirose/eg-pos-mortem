package com.fimet.parser.field.mx;



import com.fimet.parser.IEFieldFormat;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class NatTagsVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(NatTagsVarFieldParser.class);
	public NatTagsVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null && value != null) {
			try {
				IReader reader = new ByteBuffer(value);
				if (!reader.hasNext()) {
					return;
				}
				parseTags(message, reader);
			} catch (Exception e) {
				if (failOnErrorParseField) {
					throw e;
				} else {
					logger.warn(this+" error parsing "+idField+" tags childs",e);
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
		String nextTag = getNextTag(reader);
		if (nextTag == null) {
			throw new ParserException("Unknow Tag starts with: "
			+reader.toString().substring(0,Math.min(5,reader.length()-reader.position()))
			+". Tags declared: "+childs);	
		}
		return group.parse(idField+"."+nextTag, message, reader);
	}
	private String getNextTag(IReader reader) {
		for (String tag : childs) {
			if (reader.startsWith(tag)) {
				return tag;
			}
		}
		return null;
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		for (String idChild : message.getIdChildren(idField)) {
			validateTag(idChild);
			group.format(idChild, message, writer);
		}
	}
	private void validateTag(String idField) {
		String tag = idField.substring(idField.lastIndexOf('.')+1);
		for (String child : childs) {
			if (child.equals(tag)) {
				return;
			}
		}
		throw new FormatException("Unexpected Tag '"+tag+"', Tags declared: "+childs);
	}
}
