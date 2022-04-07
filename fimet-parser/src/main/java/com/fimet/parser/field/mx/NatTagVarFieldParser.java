package com.fimet.parser.field.mx;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class NatTagVarFieldParser extends VarFieldParser {

	public NatTagVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
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
}
