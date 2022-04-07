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

public class VisaDatasetsVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(VisaDatasetsVarFieldParser.class);
	public VisaDatasetsVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null && value != null) {
			try {
				IReader reader = new ByteBuffer(value);
				if (!reader.hasNext()) {
					return;
				}
				parseDatasets(message, reader);
			} catch (Exception e) {
				if (failOnErrorParseField) {
					throw e;
				} else {
					logger.warn(this+" error parsing datasets "+idField,e);
				}
			}
		}
	}
	private void parseDatasets(IMessage message, IReader reader) {
		if (!reader.hasNext()) {
			return;
		}
		while (reader.hasNext()) {
			parseDataset(reader, message);
		}
	}
	private byte[] parseDataset(IReader reader, IMessage message) {
		String nextDataset = getNextDataset(reader);
		if (nextDataset == null) {
			throw new ParserException("Unknow dataset starts with: "+reader.toString().substring(0,5)+". Datasets declared: "+childs);	
		}
		return group.parse(idField+"."+nextDataset, message, reader);
	}
	private String getNextDataset(IReader reader) {
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
			validateDataset(tag);
			group.format(idField, message, writer);
		}
	}
	private void validateDataset(String dataset) {
		for (String child : childs) {
			if (child.equals(dataset)) {
				return;
			}
		}
		throw new FormatException("Unexpected dataset '"+dataset+"', Dataset declared: "+childs);
	}
}
