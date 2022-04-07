package com.fimet.parser.field.mx;

import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.MessageUtils;
import com.fimet.utils.StringUtils;

public class NatTokensVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(NatTokensVarFieldParser.class);
	public NatTokensVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null) {
			if (value != null && value.length > 0) {
				try {
					IReader reader = new ByteBuffer(value);
					reader.move(12);
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
		if (!reader.startsWith('!')) {
			int index = reader.indexOf('!');
			if (index != -1) {
				logger.warn("Unexpected char (skip until '!'):{}, message:{}",reader, MessageUtils.toJson(message));
				reader.move(index);
			} else {
				reader.move(reader.length()-reader.position());
				logger.warn("No more tokens and buffer in not empty:'{}', message:{}",reader, MessageUtils.toJson(message));
				return null;
			}
		}
		reader.assertChar('!');
		reader.assertChar(' ');
		String tokenName = new String(reader.read(2)).toString();
		if (!childs.contains(tokenName)) {
			throw new ParserException("Unknow Token: '"+tokenName+"', tokens declared: "+childs);
		}
		reader.move(-4);
		return  group.parse(idField+"."+tokenName, message, reader);
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		/**
		 * & 0000700266
		 * 		00007:Number of Tokens
		 * 		00266: Length
		 */
		int cursor = writer.length();
		writer.append("& ");
		List<String> children = message.getIdChildren(idField);
		String numTokens = StringUtils.leftPad(""+(children.size()+1),5,'0');
		writer.append(numTokens);
		int cursorLength = writer.length();
		writer.move(5);
		String token;
		for (String idField : children) {
			token = idField.substring(idField.lastIndexOf('.')+1);
			if (!childs.contains(token)) {
				throw new FormatException("Unknow Token: '"+token+"', tokens declared: "+childs);
			}
			group.format(idField, message, writer);
		}
		String ln = StringUtils.leftPad(""+(writer.length()-cursor), 5, "0"); 
		writer.replace(cursorLength, ln);
	}
}
