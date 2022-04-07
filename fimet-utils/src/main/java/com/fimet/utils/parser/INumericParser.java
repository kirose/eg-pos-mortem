package com.fimet.utils.parser;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface INumericParser {
	public int getId();
	public String getName();
	public int parse(byte[] bytes);
	public byte[] format(int number, int length);
}
