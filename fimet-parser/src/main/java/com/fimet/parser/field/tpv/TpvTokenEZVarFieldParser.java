package com.fimet.parser.field.tpv;

import com.fimet.Manager;
import com.fimet.parser.FieldGroup;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.EncryptionUtils;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;

public class TpvTokenEZVarFieldParser extends TpvTokenVarFieldParser {

	private static final String DEFAULT_BDK = "0123456789ABCDEFFEDCBA9876543210";
	private static final String BDK = Manager.getProperty("encryption.bdk", DEFAULT_BDK);
	public TpvTokenEZVarFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
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
		return crc32(encrypt(value, message));
		//return crc32(encrypt(last4Digits(value), message));
	}
	private byte[] encrypt(byte[] value, IMessage message) {
		if (value == null || value.length != 72) {
			throw new ParserException("Cannot encrypt data from token EZ, data must be ascii");
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));

		byte[] encrypt = Converter.hexToAscii(EncryptionUtils.encrypt(
			new String(Converter.asciiToHex(ByteUtils.subArray(value, 36, 60))),
			fKeySerialNumber, 
			BDK
		).substring(0,48).getBytes());
		
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,36),
			encrypt,
			ByteUtils.subArray(value, 60,72)
		);
	}
	private byte[] crc32(byte[] value) {
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0, 64),
			EncryptionUtils.crc32hex(new String(Converter.asciiToHex(ByteUtils.subArray(value, 36, 60)))).getBytes()
		);
	}
//	private byte[] last4Digits(byte[] value) {
//		return ByteUtils.concat(
//			ByteUtils.subArray(value, 0, 60),
//			Converter.asciiToHex(ByteUtils.subArray(value, 42, 44)),
//			ByteUtils.subArray(value, 64, 72)
//		);
//	}
	private byte[] decrypt(byte[] value, IMessage message) {
		if (value == null || value.length != 72) {
			throw new ParserException("Cannot decript data from token EZ, data must be ascii");
		}
		String fKeySerialNumber = new String(ByteUtils.subArray(value, 0, 20));
		byte[] decrypt = Converter.hexToAscii(
			EncryptionUtils.decrypt(
				new String(Converter.asciiToHex(ByteUtils.subArray(value, 36, 60))),
				fKeySerialNumber,
				BDK
			).substring(0,48).getBytes()
		);
		return ByteUtils.concat(
			ByteUtils.subArray(value, 0,36),
			decrypt,
			ByteUtils.subArray(value, 60,72)
		);
	}
}
