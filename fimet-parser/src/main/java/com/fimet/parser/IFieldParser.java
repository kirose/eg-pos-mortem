package com.fimet.parser;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
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
	 * @return
	 */
	String getName();
	/**
	 * The Field Id
	 * Example:3.1
	 * Format:
	 * [0-9]+(\.[A-Za-z0-9]+)*
	 * @return
	 */
	String getIdField();
	/**
	 * Every Field has an order (address) see MessageFields
	 * Example Field 3.1, order=3.1
	 * Format:
	 * [0-9]+(\.[0-9]+)*
	 * @return
	 */
	String getIdOrder();
	/**
	 * A Regexp for validate the field value
	 * Example:
	 * [A-Za-z]* only Letters
	 * @return
	 */
	String getMask();
	/**
	 * If the field is Fixed then
	 * return the expected field value length
	 * If the field is Variable then
	 * return the length of variable length
	 * for variable length a converter length is required
	 * @return
	 */
	int getLength();
	/**
	 * 
	 * @param value
	 * @return return true if the value matches with his Regexp mask, false in other case
	 */
	boolean isValidValue(String value);
	/**
	 * 
	 * @param value
	 * @return true if the value has a valid length, false in other case
	 */
	boolean isValidLength(String value);
	/**
	 * The address of the Field in MessageFields Tree
	 * Example: Field 3.1 with address=[3,1]
	 * @return
	 */
	short[] getAddress();
	/**
	 * Parse the byte array message in IReader to IMessage 
	 * @param reader
	 * @param message
	 * @return
	 * @throws ParserException
	 */
	byte[] parse(IReader reader, IMessage message) throws ParserException;
	/**
	 * Format the IMessage to writer 
	 * @param writer
	 * @param message
	 * @return
	 * @throws ParserException
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
