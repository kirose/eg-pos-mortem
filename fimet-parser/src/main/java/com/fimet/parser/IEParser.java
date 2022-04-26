package com.fimet.parser;

/**
 * 
 * @author Marco A. Salazar
 *
 */
public interface IEParser {
	/**
	 * The Parser name
	 * @return the name
	 */
	public String getName();
	/**
	 * The  Field Group Name associated
	 * @return the field group
	 */
	public String getFieldGroup();
	/**
	 * Message converter (no MLI conversion) 
	 * Example:
	 * Converter=HEX_TO_ASCII
	 * 46494D4554 to FIMET
	 * @return the converter
	 */
	public String getConverter();
	/**
	 * The parser class must implements IParser 
	 * Example:com.fimet.parser.MyParser
	 * @return the parser class
	 */
	public String getParserClass();
}
