package com.fimet.parser.field;


import com.fimet.parser.AbstractFieldParser;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

/**
 * A parser for fixed length field 
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class FixedFieldParser extends AbstractFieldParser {

	private Integer length;
	public FixedFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
		this.length = fieldFormat.getLength();
	}
	@Override
	public byte[] parseValue(IReader reader, IMessage message) {
		byte[] value = reader.read(length);
		value = converterValue.convert(value);
		return value;
	}
	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		value = converterValue.deconvert(value);
		writer.append(value);
		if (value.length != length){
			throw new FormatException(this+" - Invalid length ("+value.length+") on field "+getIdField()+" expected fixed length: "+length);
		}
		return value;
	}
	public boolean isValidLength(String value) {
		return converterValue.deconvert(value.getBytes()).length == length;
	}
	public boolean isFixed() {
		return true;
	}
}
