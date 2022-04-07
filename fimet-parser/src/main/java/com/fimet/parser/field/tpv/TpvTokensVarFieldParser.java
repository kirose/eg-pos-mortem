package com.fimet.parser.field.tpv;


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

public class TpvTokensVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(TpvTokensVarFieldParser.class);
	public TpvTokensVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (value != null && !new String(value).startsWith("00")) {// Cierre de lotes no se parsean sus hijos
			if (childs != null) {
				try {
					IReader reader = new ByteBuffer(value);
					if (reader.hasNext()) {
						parseTokens(reader, message);
					}
				} catch (Exception e) {
					if (failOnErrorParseField) {
						throw e;
					} else {
						logger.warn(this+" error parsing tokens "+idField,e);
					}
				}
			}
		}
	}
	private void parseTokens(IReader reader, IMessage message) {
		while (reader.hasNext()) {
			parseToken(reader, message);
		}		
	}
	private byte[] parseToken(IReader reader, IMessage message) {
		String tokenName = new String(reader.read(2));
		if (!childs.contains(tokenName)) {
			throw new ParserException("Unknow Token: '"+tokenName+"', tokens declared: "+childs);
		}
		reader.move(-2);
		return group.parse(idField+"."+tokenName, message, reader);
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		String token;
		for (String idChild : message.getIdChildren(idField)) {
			token = idChild.substring(idChild.lastIndexOf('.')+1);
			if (!childs.contains(token)) {
				throw new FormatException("Unknow Token: '"+token+"', tokens declared: "+childs);
			}
			group.format(idChild, message, writer);
		}
	}
}
