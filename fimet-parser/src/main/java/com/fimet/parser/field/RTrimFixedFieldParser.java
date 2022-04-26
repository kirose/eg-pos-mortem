package com.fimet.parser.field;


import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

/**
 * Parser for MessageFields from the message 
 * 
 * @author Marco A. Salazar
 *
 */
public class RTrimFixedFieldParser extends FixedFieldParser {

	private Integer length;
	public RTrimFixedFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
		this.length = fieldFormat.getLength();
	}
	@Override
	public byte[] parseValue(IReader reader, IMessage message) {
		if (length % 2 == 0) {
			byte[] value = reader.read(length);
			value = converterValue.convert(value);
			return value;
		} else {
			byte[] value = reader.read(length+1);
			value = converterValue.convert(value);
			value = ByteUtils.subArray(value, 0,value.length-1);
			return value;
		}
	}
	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		if (length % 2 == 0) {
			value = converterValue.deconvert(value);
			writer.append(value);
		} else {
			value = converterValue.deconvert(ByteUtils.append(value,(byte)48));
			writer.append(value);
		}
		if ((length % 2 == 0 && value.length != length) || (length % 2 != 0 && value.length -1 != length)){
			throw new FormatException(this+" - Invalid length ("+value.length+") on field "+getIdField()+" expected fixed length: "+length);
		}
		return value;
	}
}
