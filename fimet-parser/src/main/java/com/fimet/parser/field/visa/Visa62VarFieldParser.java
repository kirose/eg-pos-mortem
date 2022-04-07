package com.fimet.parser.field.visa;

import java.util.ArrayList;
import java.util.List;

import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;

public class Visa62VarFieldParser extends VarFieldParser {

	private static final String EMPTY_BITMAP = "0000000000000000000000000000000000000000000000000000000000000000";
	
	public Visa62VarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null) {
			IReader reader = new ByteBuffer(value);//Bytes in Hex ebcdict
			if (reader.hasNext()) {
				List<Integer> bitmap = parseBitmap(reader);
				for (Integer index : bitmap) {
					group.parse(idField+"."+index, message, reader);
				}
			}
		}
	}
	public List<Integer> parseBitmap(IReader reader) {
		List<Integer> bitmap = new ArrayList<>();
		String bm = new String(Converter.hexToBinary(reader.read(16)));
		for (int i = 0; i < bm.length(); i++) {
			if (bm.charAt(i) == '1') {
				bitmap.add(i+1);
			}
		}
		return bitmap;
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		formatBitmap(writer, message);
		for (String idChild : message.getIdChildren(idField)) {
			group.format(idChild, message, writer);
		}
	}
	private void formatBitmap(IWriter writer, IMessage message) {
		StringBuilder bitmap = new StringBuilder().append(EMPTY_BITMAP);
		int index = 0;
		for (String idField : message.getIdChildren(idField)) {
			index = Integer.parseInt(idField.substring(idField.lastIndexOf('.')+1));
			bitmap.replace(index-1, index, "1");
		}
		writer.append(Converter.binaryToHex(bitmap.toString().getBytes()));
	}
}
