package com.fimet.parser.field.tpv;

import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class TpvTokenVarFieldParser extends VarFieldParser {

	public TpvTokenVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(2);
		return super.parse(reader, message);
	}
	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		writer.append(key);
		int index = writer.length(); 
		writer.move(length);

		value = converterValue.deconvert(value);
		writer.append(value);
		
		int length = value.length;
		if (maxLength != null && length > maxLength) {
			throw new FormatException("Field "+this.idField+" ("+length+") Exceed MaxLength: "+ maxLength);
		}
		writer.replace(index, parserLength.format(length, this.length));
		return value;
	}
}
