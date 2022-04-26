package com.fimet.parser;



/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
public interface IEFieldFormat {
	/**
	 * An valid field id: 3.1
	 * @return the field id
	 */
	public String getIdField();
	/**
	 * The Field Group
	 * @return the group name
	 */
	public String getGroup();
	/**
	 * The field parent id, possible null
	 * @return the id field parent
	 */
	public String getIdFieldParent();
	/**
	 * The child name
	 * Example:
	 * Parent Field id: 3
	 * Children: 1,2,3
	 * Id Field Children: 3.1,3.2,3.3
	 * @return the children
	 */
	public String getChilds();
	/**
	 * Converter Value
	 * Example:
	 * ASCII-EBCDIC
	 * HEX-ASCII
	 * @return the converter value name
	 */
	public String getConverterValue();
	/**
	 * Converter Length
	 * Example:
	 * ASCII-EBCDIC
	 * HEX-ASCII
	 * @return the converter length name
	 */
	public String getConverterLength();
	/**
	 * A numeric Parser Length
	 * Example:
	 * HEX-DEC: FF-255
	 * @return the parser length
	 */
	public String getParserLength();
	/**
	 * A Regexp for validate the field value
	 * Example:
	 * [A-Za-z]* only Letters
	 * @return the pattern for validations
	 */
	public String getMask();
	/**
	 * If the field is Fixed then
	 * return the expected field value length
	 * If the field is Variable then
	 * return the length of variable length
	 * for variable length a converter length is required
	 * @return the length
	 */
	public Integer getLength();
	/**
	 * If the field is Variable then Max Length is required  
	 * @return the max length
	 */
	public Integer getMaxLength();
	/**
	 * The field Name 
	 * @return the name
	 */
	public String getName();
	/**
	 * The class parser instance of IFieldParser
	 * @return the class parser
	 */
	public String getClassParser();
	/**
	 * Every Field has an order (address) see MessageFields
	 * Example Field 3, children=[]:
	 * Field 3, insert 3.2, children-[3.2]
	 * Field 3, insert 3.3, children-[3.2,3.3]
	 * Field 3, insert 3.1, children-[3.1,3.2,3.3]
	 * @return the order for address
	 */
	public String getOrder();
}
