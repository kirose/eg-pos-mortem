package com.fimet.parser;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
/**
 * 
 * @author Marco A. Salazar
 *
 */
public interface IFieldParser {
	
	/**
	 * 
	 * @return true if the FieldParser extends from FixedFieldParser, false in other case
	 */
	boolean isFixed();
	/**
	 * The Field Name
	 * @return the name
	 */
	String getName();
	/**
	 * The Field Id
	 * Example:3.1
	 * Format:
	 * [0-9]+(\.[A-Za-z0-9]+)*
	 * @return the id field
	 */
	String getIdField();
	/**
	 * Every Field has an order (address) see MessageFields
	 * Example Field 3.1, order=3.1
	 * Format:
	 * [0-9]+(\.[0-9]+)*
	 * @return the id order 
	 */
	String getIdOrder();
	/**
	 * A Regexp for validate the field value
	 * Example:
	 * [A-Za-z]* only Letters
	 * @return the pattern for validation
	 */
	String getMask();
	/**
	 * If the field is Fixed then
	 * return the expected field value length
	 * If the field is Variable then
	 * return the length of variable length
	 * for variable length a converter length is required
	 * @return the length
	 */
	int getLength();
	/**
	 * 
	 * @param value to be validated
	 * @return return true if the value matches with his regexp mask, false in other case
	 */
	boolean isValidValue(String value);
	/**
	 * 
	 * @param value to be validated
	 * @return true if the value has a valid length, false in other case
	 */
	boolean isValidLength(String value);
	/**
	 * The address of the Field in MessageFields Tree
	 * Example: Field 3.1 with address=[3,1]
	 * @return the address for tree manipulation
	 */
	short[] getAddress();
	/**
	 * Parse the byte array message in IReader to IMessage 
	 * @param reader class for manipulate bytes
	 * @param message to save parsed field
	 * @return the parsed bytes
	 * @throws ParserException in case of a parse error
	 */
	byte[] parse(IReader reader, IMessage message) throws ParserException;
	/**
	 * Format the IMessage to writer 
	 * @param writer to format the field
	 * @param message to be formated
	 * @return the formatted bytes
	 * @throws FormatException in case of a format error
	 */
	byte[] format(IWriter writer, IMessage message) throws FormatException;
	/**
	 * 
	 * @return true if the field has children declared, false in other case
	 */
	boolean hasChildren();
	/**
	 * 
	 * @param idChild is the child key name
	 * Example: call hasChild("1") will return true for field 3 with children [3.1,3.2,3.3]
	 * @return return true if the field has declared the child idChild, false in other case
	 */
	boolean hasChild(String idChild);
	/**
	 * 
	 * @param idChild is the child key name
	 * Example: call indexOfChild("2") will return 1 for field 3 with children [3.1,3.2,3.3]
	 * @return the child index
	 */
	int indexOfChild(String idChild);
	/**
	 * Example: call getIdChild(2) will return "3" for field 3 with children [3.1,3.2,3.3]
	 * @param index is the child index
	 * @return the child id
	 */
	String getIdChild(int index);
	
	boolean isRoot();
}
