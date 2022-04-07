package com.fimet.parser.field.mx;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.FixedFieldParser;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.utils.parser.INumericParser;
import com.fimet.utils.parser.NumericParser;

public class NatTagFixedFieldParser extends FixedFieldParser {

	protected final IConverter  converterLength;
	protected final INumericParser parserLength;
	public NatTagFixedFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
		this.converterLength = Converter.getConverter(fieldFormat.getConverterLength());
		this.parserLength = NumericParser.getParser(fieldFormat.getParserLength());
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(converterLength.deconvert(key.getBytes()).length+2);
		return super.parse(reader, message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(converterLength.deconvert(key.getBytes()));
		int index = writer.length();
		writer.move(2);
		byte[] value = super.format(writer, message);
		writer.replace(index, converterLength.deconvert(parserLength.format(value.length, 2)));
		return value;
	}
}
