package com.fimet.parser;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IEParser {
	/**
	 * The Parser name
	 * @return
	 */
	public String getName();
	/**
	 * The  Field Group Name associated
	 * @return
	 */
	public String getFieldGroup();
	/**
	 * Message converter (no MLI conversion) 
	 * Example:
	 * Converter=HEX_TO_ASCII
	 * 46494D4554->FIMET
	 * @return
	 */
	public String getConverter();
	/**
	 * The parser class must implements IParser 
	 * Example:com.fimet.parser.MyParser
	 * @return
	 */
	public String getParserClass();
}
