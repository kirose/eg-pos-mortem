package com.fimet.utils;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IWriter {
	void move(int offser);
	void append(String text);
	void append(byte b);
	void append(byte[] bytes);
	void preappend(String text);
	void preappend(byte[] bytes);
	void insert(int index, byte[] bytes);
	void insert(int index, String text);
	void replace(int index, byte[] bytes);
	void replace(int index, String text);
	byte[] getBytes();
	int length();
}
