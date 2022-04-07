package com.fimet.parser;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public interface IParser {

	/**
	 * 
	 * @return the parser name
	 */
	String getName();
	/**
	 * 
	 * @return The FieldGroup associated
	 */
	IFieldGroup getFieldGroup();
	byte[] formatMessage(IMessage msg) throws FormatException;
	IMessage parseMessage(byte[] bytes) throws ParserException;
	byte[] parseField(String idField, IMessage message, IReader reader) throws ParserException;
	byte[] parseField(int idField, IMessage message, IReader reader) throws ParserException;
	void formatField(String idField, IMessage message, IWriter writer) throws FormatException;
	void formatField(int idField, IMessage message, IWriter writer) throws FormatException;
}
