package com.fimet.parser;

import java.util.List;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public interface IFieldGroup {
	/**
	 * The Field Group Name
	 * @return
	 */
	public String getName();
	/**
	 * Parse the Field idField from IReader to IMessage
	 * @param idField
	 * @param message
	 * @param reader
	 * @return value parsed
	 */
	public byte[] parse(String idField, IMessage message, IReader reader);
	/**
	 * Parse the Field idField from IReader to IMessage
	 * @param idField
	 * @param message
	 * @param reader
	 * @return value parsed
	 */
	public byte[] parse(int idField, IMessage message, IReader reader);
	/**
	 * Format the Field idField to IReader from IMessage
	 * @param idField
	 * @param message
	 * @param writer
	 */
	public void format(String idField, IMessage message, IWriter writer);
	/**
	 * Format the Field idField to IReader from IMessage
	 * @param idField
	 * @param message
	 * @param writer
	 */
	public void format(int idField, IMessage message, IWriter writer);
	/**
	 * The Address of the field idField associated
	 * Every Field has an order (address) see MessageFields
	 * Example Field 3.2 address=[3,2]:
	 * @param idField
	 * @return Address
	 */
	public short[] getAddress(String idField);
	/**
	 * The Field Group Parent
	 * @return
	 */
	public IFieldGroup getParent();
	/**
	 * The Children Field Group
	 * @return
	 */
	public List<IFieldGroup> getChildren();
	/**
	 * The Field Parsers roots
	 * Example:
	 * FieldGroup:
	 * 3 -> [3.1,3.2,3.3]
	 * 4 -> []
	 * 5 -> []
	 * Roots: [3,4,5]
	 * @return
	 */
	public List<IFieldParser> getRoots();
	/**
	 * Return the  FieldParser associated to idFieldParser
	 * @param idFieldParent
	 * @return
	 */
	public IFieldParser getFieldParser(String idFieldParent);
}
