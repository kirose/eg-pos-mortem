package com.fimet.parser.field;



import com.fimet.parser.IEFieldFormat;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

/**
 * Useful for visa and tpv's
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class LTrimVarFieldParser extends VarFieldParser {
	private static Logger logger = LoggerFactory.getLogger(LTrimVarFieldParser.class);
	public LTrimVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	public byte[] parseValue(IReader reader, IMessage mesage) {
		int length = parserLength.parse(converterLength.convert(reader.read(this.length)));
		if (length % 2 == 0) {
			byte[] bytes = reader.read(length);
			if (bytes.length != length)
				logger.warn(this+", expected length "+length+" current length: "+bytes.length);
			return converterValue.convert(bytes);
		} else {
			byte[] bytes = reader.read(length+1);
			if (bytes.length != length+1)
				logger.warn(this+", expected length "+(length+1)+" current length: "+bytes.length);
			return ByteUtils.subArray(converterValue.convert(bytes),1);
		}
	}
	@Override
	public byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		int index = writer.length(); 
		writer.move(length);
		int length = value.length;
		if (length % 2 == 0) {
			value = converterValue.deconvert(value);
			writer.append(value);
			writer.replace(index, parserLength.format(length, this.length));
		} else {
			value = converterValue.deconvert(ByteUtils.preappend(value,(byte)48));
			writer.append(value);
			writer.replace(index, parserLength.format(length, this.length));			
		}
		if (maxLength != null && length > maxLength) {
			throw new FormatException("Field "+this.idField+" ("+length+") Exceed MaxLength: "+ maxLength);
		}
		return value;
	}
}
