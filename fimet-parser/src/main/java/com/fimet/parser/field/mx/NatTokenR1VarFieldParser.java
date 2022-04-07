package com.fimet.parser.field.mx;

import com.fimet.Manager;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.EncryptionUtils;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;

public class NatTokenR1VarFieldParser extends NatTokenVarFieldParser {

	private static final String DEFAULT_BDK = "0123456789ABCDEFFEDCBA9876543210";
	private static final String BDK = Manager.getProperty("encryption.bdk", DEFAULT_BDK);
	public NatTokenR1VarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super(fieldGroup, fieldFormat);
	}
	@Override
	protected byte[] posParseValue(byte[] value, IMessage message) {
		value = decrypt(value, message);
		return super.posParseValue(value, message);
	}
	@Override
	protected byte[] preFormatValue(IWriter writer, IMessage message) {
		byte[] value = super.preFormatValue(writer, message);
		return crc32(encrypt(last4Digits(value), message));
	}
	private byte[] encrypt(byte[] value, IMessage message) {
		if (value == null || value.length != 266) {
			throw new ParserException("Cannot encrypt data from token R1, data must be ascii");
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));

		byte[] encrypt = EncryptionUtils.encrypt(
			new String(ByteUtils.subArray(value, 29, 253)),
			fKeySerialNumber, 
			BDK
		).substring(0,224).getBytes();
		
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,29),
			encrypt,
			ByteUtils.subArray(value, 253,266)
		);
	}
	private byte[] crc32(byte[] value) {
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0, 258),
			EncryptionUtils.crc32hex(new String(ByteUtils.subArray(value, 29, 253))).getBytes()
		);
	}
	private byte[] last4Digits(byte[] value) {
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0, 254),
			Converter.hexToAscii(ByteUtils.subArray(value, 105, 113)),
			ByteUtils.subArray(value, 258, 266)
		);
	}
	private byte[] decrypt(byte[] value, IMessage message) {
		if (value == null || value.length != 266) {
			throw new ParserException("Cannot decript data from token R1, expected length 266 current "+(value != null ? value.length : 0));
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));
		byte[] decrypt = EncryptionUtils.decrypt(
			new String(ByteUtils.subArray(value, 29, 253)),//+"FFFFFFFF",//new String(ByteUtils.subArray(value, 38, 86)),
			fKeySerialNumber,
			BDK
		).substring(0,224).getBytes();
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,29),
			decrypt,
			ByteUtils.subArray(value, 253,266)
		);
	}
}
