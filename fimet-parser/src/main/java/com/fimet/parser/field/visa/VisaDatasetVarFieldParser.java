package com.fimet.parser.field.visa;



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

public class VisaDatasetVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(VisaDatasetVarFieldParser.class);
	public VisaDatasetVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(key.length());
		return super.parse(reader, message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(converterValue.deconvert(key.getBytes()));
		return super.format(writer, message);
	}
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
					logger.warn(this+" error parsing tokens "+idField,e);
				}
			}
		}
	}
	private void parseTags(IMessage message, IReader reader) {
		if (!reader.hasNext()) {
			return;
		}
		while (reader.hasNext()) {
			parseTag(reader, message);
		}
	}
	private byte[] parseTag(IReader reader, IMessage message) {
		String nextTag = getNextTag(reader);
		if (nextTag == null) {
			throw new ParserException("Unknow Tag starts with: "+reader.toString().substring(0,5)+".Tags declared: "+childs);	
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
		for (String idField : message.getIdChildren(idField)) {
			String tag = idField.substring(idField.lastIndexOf('.')+1);
			validateTag(tag);
			group.format(idField, message, writer);
		}
	}
	private void validateTag(String tag) {
		for (String child : childs) {
			if (child.equals(tag)) {
				return;
			}
		}
		throw new FormatException("Unexpected Tag '"+tag+"', Tags declared: "+childs);
	}
}
