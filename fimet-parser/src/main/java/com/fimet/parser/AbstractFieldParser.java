package com.fimet.parser;

import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.utils.Args;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.ByteBuilder;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;

import com.fimet.Manager;

/**
 * Parser for MessageFields from the message 
 * 
 * @author Marco Antonio
 *
 */
public abstract class AbstractFieldParser implements IFieldParser {
	private static Logger logger = LoggerFactory.getLogger(AbstractFieldParser.class);
	protected static final boolean failOnInvalidFormat = Manager.getPropertyBoolean("parser.failOnInvalidFormat", false);
	protected static final boolean failOnErrorParseField = Manager.getPropertyBoolean("parser.failOnErrorParseField", false);
	
	protected final String idFieldParent;
	protected final String idField;
	protected final String idOrder;
	protected final short[] address;
	protected final String key;
	protected final String name;
	protected final String mask;
	protected final IConverter  converterValue;
	protected final List<String> childs;
	protected final int length;
	protected final IFieldGroup group;
	
	public AbstractFieldParser(FieldGroup fieldGroup, IEFieldFormat fieldFormat) {
		super();
		Args.notNull("FieldParser Entity", fieldFormat);
		Args.notBlank("FieldParser Mask", fieldFormat.getMask());
		Args.notBlank("FieldParser IdField", fieldFormat.getIdField());
		Args.notBlank("FieldParser Name", fieldFormat.getName());
		Args.notNull("FieldParser Length", fieldFormat.getLength());
		Args.notNull("FieldParser Order", fieldFormat.getOrder());
		
		this.length = fieldFormat.getLength();
		this.idFieldParent = fieldFormat.getIdFieldParent();
		this.idField = fieldFormat.getIdField();
		this.idOrder = fieldFormat.getOrder();
		int index = idField.lastIndexOf('.');
		if (index != -1) {
			key = idField.substring(index+1);
		} else {
			key = idField;
		}
		this.mask = fieldFormat.getMask();
		this.name = fieldFormat.getName();
		this.group = fieldGroup;
		if (fieldFormat.getChilds() != null) {
			this.childs = Arrays.asList(fieldFormat.getChilds().split(","));
		} else {
			this.childs = null;
		}
		this.converterValue = fieldFormat.getConverterValue()!=null?Converter.getConverter(fieldFormat.getConverterValue()):Converter.NONE;
		String[] orders = idOrder.split("\\.");
		this.address = new short[orders.length];
		int i = 0;
		for (String o : orders) {
			address[i++] = Short.parseShort(o); 
		}
	}
	/**
	 * Parse an field to Msg
	 * @return Field an field parsed
	 */
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		preParseValue(reader, message);
		byte[] value = parseValue(reader, message);
		return posParseValue(value, message);
	}
	
	abstract public byte[] parseValue(IReader reader, IMessage message);
	
	protected void preParseValue(IReader reader, IMessage message) {}
	
	protected byte[] posParseValue(byte[]value, IMessage message) {
		message.setValue(idField, value);
		applyRuleType(value);
		parseChilds(value, message);
		return message.getValueAsBytes(idField);
	}
	
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null) {
			IReader reader = new ByteBuffer(value);
			byte[] child;
			String idChild;
			for (String childIndex : childs) {
				try {
					idChild = idField + "." + childIndex;
					child = group.parse(idChild, message, reader);
					message.setValue(idChild, child);
				} catch (Exception e) {
					if (failOnErrorParseField) {
						throw e;
					} else {
						logger.warn(this+" fail parsing subfield '"+idField + "." + childIndex+"'",e);
					}
				}
			}
		}		
	}
	/**
	 * Format the field from Msg
	 */
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		byte[] value = preFormatValue(writer, message);
		value = formatValue(writer, message, value);
		posFormatValue(value, message);
		return value;
	}
	
	abstract public byte[] formatValue(IWriter writer, IMessage message, byte[] value);

	protected byte[] preFormatValue(IWriter writer, IMessage message) {
		byte[] value;
		if (message.hasChildren(idField)) {
			ByteBuilder temp = new ByteBuilder();
			formatChilds(temp, message);
			value = temp.getBytes();
		} else {
			value = message.getValueAsBytes(idField);
		}
		applyRuleType(value);
		return value;
	}
	protected void posFormatValue(byte[] value, IMessage message) {}
	
	protected void formatChilds(IWriter writer, IMessage message) {
		String idChild;
		for (String child : childs) {
			idChild = idField+"."+child;
			if (!message.hasField(idChild)) {
				throw new FormatException(this+" expected subfield: "+idChild);
			}
			group.format(idChild, message, writer);
		}
	}
	protected void applyRuleType(byte[] value) {
		if (childs == null && !new String(value).matches(mask)) {
			boolean failOnInvalidFormat = Manager.getPropertyBoolean("parser.failOnInvalidFormat", false);
			if (failOnInvalidFormat) {
				throw new FormatException(this+" expected format '"+mask+"' found: '"+new String(value)+"'");
			} else if (logger.isWarnEnabled()) {
				logger.warn(this+" expected format '"+mask+"' found: '"+new String(value)+"'");
			}
		}
	}
	protected IFieldParser getParent(){
		return idFieldParent != null ? group.getFieldParser(idFieldParent) : null;
	}
	public IFieldGroup getGroup() {
		return group;
	}
	public boolean hasChild(String idChild) {
		return childs != null && childs.contains(idChild);
	}
	public int indexOfChild(String idChild) {
		return childs != null ? childs.indexOf(idChild) : -1;
	}
	public String getIdChild(int index) { 
		return childs.get(index);
	}
	public String getName() {
		return name;
	}
	public String getIdField() {
		return idField;
	}
	public String getIdOrder() {
		return idOrder;
	}
	public String getMask() {
		return mask;
	}
	public int getLength() {
		return length;
	}
	public short[] getAddress() {
		return address;
	}
	public boolean hasChildren() {
		return childs != null && !childs.isEmpty();
	}
	@Override
	public boolean isValidValue(String value) {
		return value != null && value.matches(mask); 
	}
	public boolean isRoot() {
		return this.address.length == 1;
	}
	@Override
	public String toString() {
		return "Field \""+ idField+"\" in group \""+group+"\""; 
	}
}
