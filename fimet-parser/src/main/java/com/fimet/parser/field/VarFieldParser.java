package com.fimet.parser.field;



import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.Manager;
import com.fimet.parser.AbstractFieldParser;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.utils.parser.INumericParser;
import com.fimet.utils.parser.NumericParser;

/**
 * A parser for field variable length 
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class VarFieldParser extends AbstractFieldParser {
	private static Logger logger = LoggerFactory.getLogger(VarFieldParser.class);
	private static final boolean failOnInvalidFormat = Manager.getPropertyBoolean("parser.failOnInvalidFormat", false);
	
	protected Integer maxLength;
	protected final IConverter  converterLength;
	protected final INumericParser parserLength;
	
	public VarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
		this.maxLength = fieldFormat.getMaxLength();
		this.converterLength = Converter.getConverter(fieldFormat.getConverterLength());
		this.parserLength = NumericParser.getParser(fieldFormat.getParserLength());
	}
	@Override
	public byte[] parseValue(IReader reader, IMessage message) {
		int length = parserLength.parse(converterLength.convert(reader.read(this.length)));
		byte[] bytes = reader.read(length);
		if (bytes.length != length) {
			if (failOnInvalidFormat) {
				throw new FormatException(this+" length parsed ("+length+") exceed max length: "+ maxLength);	
			} else if (logger.isWarnEnabled()){
				logger.warn(this+" length parsed ("+length+") exceed max length: "+ maxLength);
			}
		}
		return converterValue.convert(bytes);
	}
	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		int index = writer.length(); 
		writer.move(this.length);

		if (maxLength != null && value.length > maxLength) {
			if (failOnInvalidFormat) {
				throw new FormatException(this+" length parsed ("+length+") exceed max length: "+ maxLength);
			} else if (logger.isWarnEnabled()){
				logger.warn(this+" length formated ("+length+") exceed max length: "+ maxLength);
			}
		}
		value = converterValue.deconvert(value);
		writer.append(value);
		
		writer.replace(index, converterLength.deconvert(parserLength.format(value.length, this.length)));
		return value;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public boolean isValidLength(String value) {
		try {
			return converterValue.deconvert(value.getBytes()).length <= maxLength;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean isFixed() {
		return false;
	}
}
