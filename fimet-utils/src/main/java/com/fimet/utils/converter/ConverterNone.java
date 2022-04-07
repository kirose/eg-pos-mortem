package com.fimet.utils.converter;

import com.fimet.utils.converter.Converter;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ConverterNone extends Converter {
	ConverterNone(int id, String name) {
		super(id, name);
	}

	@Override
	public byte[] convert(byte[] bytes) {
		return bytes;
	}

	@Override
	public byte[] deconvert(byte[] bytes) {
		return bytes;
	}
}
