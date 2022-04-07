package com.fimet.parser.field.mx;

import com.fimet.Manager;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.EncryptionUtils;
import com.fimet.utils.IWriter;

public class NatTokenEZVarFieldParser extends NatTokenVarFieldParser {

	private static final String DEFAULT_BDK = "0123456789ABCDEFFEDCBA9876543210";
	private static final String BDK = Manager.getProperty("encryption.bdk", DEFAULT_BDK);
	public NatTokenEZVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
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
		if (value == null || value.length != 98) {
			throw new ParserException("Cannot encrypt data from token EZ, data must be ascii");
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));

		byte[] encrypt = EncryptionUtils.encrypt(
			new String(ByteUtils.subArray(value, 38, 86)),
			fKeySerialNumber, 
			BDK
		).substring(0,48).getBytes();
		
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,38),
			encrypt,
			ByteUtils.subArray(value, 86,98)
		);
	}
	private byte[] crc32(byte[] value) {
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0, 90),
			EncryptionUtils.crc32hex(new String(ByteUtils.subArray(value, 38, 86))).getBytes()
		);
	}
	private byte[] last4Digits(byte[] value) {
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0, 86),
			ByteUtils.subArray(value, 50, 54),
			ByteUtils.subArray(value, 90, 98)
		);
	}
	private byte[] decrypt(byte[] value, IMessage message) {
		if (value == null || value.length != 98) {
			throw new ParserException("Cannot decript data from token EZ, data must be ascii");
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));
		byte[] decrypt = EncryptionUtils.decrypt(
			new String(ByteUtils.subArray(value, 38, 86)),
			fKeySerialNumber,
			BDK
		).substring(0,48).getBytes();
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,38),
			decrypt,
			ByteUtils.subArray(value, 86,98)
		);
	}
}
