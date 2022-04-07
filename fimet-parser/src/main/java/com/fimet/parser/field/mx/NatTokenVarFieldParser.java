package com.fimet.parser.field.mx;

import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class NatTokenVarFieldParser extends VarFieldParser {

	public NatTokenVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	public byte[] parseValue(IReader reader, IMessage message) {
		reader.assertChar('!');
		reader.assertChar(' ');
		reader.move(2);
		byte[] bytes = reader.read(this.length);
		bytes = converterLength.convert(bytes);
		int length = parserLength.parse(bytes);
		reader.assertChar(' '); 
		byte[] value = reader.read(length);
		value = converterValue.convert(value);
		return value;
	}

	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		writer.append("! ");
		writer.append(key);
		int index = writer.length(); 
		writer.move(length);
		writer.append(" ");

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
