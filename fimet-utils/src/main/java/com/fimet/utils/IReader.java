package com.fimet.utils;

import com.fimet.utils.converter.IConverter;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IReader {
	boolean startsWith(String text);
	int length();
	int position();
	void move(int offset);
	void assertChar(char c);
	boolean startsWith(char c);
	boolean hasNext();
	byte[] peek(int length);
	byte[] read(int length);
	void convert(IConverter converter);
	public int indexOf(char b);
}
