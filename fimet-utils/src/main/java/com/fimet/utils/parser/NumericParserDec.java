package com.fimet.utils.parser;

import com.fimet.utils.StringUtils;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class NumericParserDec extends NumericParser {
	
	NumericParserDec(int id, String name) {
		super(id, name);
	}

	@Override
	public int parse(byte[] ascii) {
		return Integer.parseInt(new String(ascii));
	}

	@Override
	public byte[] format(int number, int length) {
		String fmt = Integer.toString(number);
		if (fmt.length() == length) {
			return fmt.getBytes();
		} else if (fmt.length() < length) {
			return StringUtils.leftPad(fmt, length, '0').getBytes();
		} else {
			throw new NumberFormatException("Cannot format number "+number+" to "+length+" digits");
		}
	}
}
