package com.fimet.parser;

import com.google.gson.annotations.JsonAdapter;
import com.fimet.FimetException;
import com.fimet.json.IMessageAdapter;
import com.fimet.utils.MessageUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The message of the transaction
 * 
 * @author Marco A. Salazar
 *
 */
@JsonAdapter(IMessageAdapter.class)
public class Message implements Serializable, Cloneable, IMessage {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static final byte[] VALUE_EMPTY = new byte[0];

	protected Map<String, Object> properties;
	protected MessageFields fields;
	public Message() {
		properties = new HashMap<String, Object>();
		this.fields = new MessageFields(this);
	}
	public IParser getParser() {
		return (IParser)properties.get(PARSER);
	}
	public void setParser(IParser parser) {
		setProperty(PARSER, parser);
	}
	public List<Field> getRootsAsList() {
		return new FieldTree(fields).getRootsAsList();
	}
	public Map<String, Field> getRootsAsMap() {
		return new FieldTree(fields).getRootsAsMap();
	}
	@Override
	public String toString() {
		return MessageUtils.toJsonPretty(this);
	}
	@Override
	public boolean hasField(int idField) {
		return fields.find(idField+"") != null;
	}
	@Override
	public boolean hasField(String idField) {
		return fields.find(idField) != null;
	}
	@Override
	public byte[] getValueAsBytes(int idField) {
		return fields.get(idField+"");
	}
	@Override
	public byte[] getValueAsBytes(String idField) {
		return fields.get(idField);
	}
	@Override
	public void remove(int idField) {
		fields.remove(idField+"");
	}
	@Override
	public void remove(String idField) {
		fields.remove(idField);
	}
	@Override
	public void removeAll(String ...idFields) {
		for (String idField : idFields) {
			fields.remove(idField);
		}
	}
	@Override
	public void removeAll(Collection<String> all) {
		for (String idField : all) {
			fields.remove(idField);
		}
	}
	public void replace(String idField, String value) {
		fields.replace(idField, value != null ? value.getBytes() : null);
	}
	public void replace(String idField, byte[] value) {
		fields.replace(idField, value);
	}
	public String getValue(String idField) {
		byte[] bs = fields.get(idField);
		return bs != null ? new String(bs) : null;
	}
	public String getValue(int idField) {
		byte[] bs = fields.get(""+idField);
		return bs != null ? new String(bs) : null;
	}
	public void setValue(int idField, String value) {
		setValue(""+idField, value == null ? VALUE_EMPTY : value.getBytes());
	}
	public void setValue(String idField, String value) {
		setValue(idField, value == null ? VALUE_EMPTY : value.getBytes());
	}
	@Override
	public void setValue(int idField, byte[] value) {
		fields.insert(""+idField, value);
	}
	@Override
	public void setValue(String idField, byte[] value) {
		fields.insert(idField, value);		
	}
	public Message clone(List<String> excludeFields) {
		Message msg;
		try {
			msg = this.clone();
		} catch (CloneNotSupportedException e) {
			throw new FimetException("Cannot clone message",e);
		}
		if (excludeFields != null) {
			for (String idField : excludeFields) {
				msg.remove(idField);
			}
		}
		return msg;
	}
	public Message clone() throws CloneNotSupportedException {
		Message clone = new Message();
		clone.properties = new HashMap<String,Object>();
		clone.properties.putAll(properties);
		if (fields != null) {
			clone.fields = this.fields.clone();
		}
		return clone; 
	}
	public int[] getBitmap() {
		return fields.getBitmap();
	}
	public boolean hasChildren(String idField) {
		return fields.hasChildren(idField);
	}
	public List<String> getIdChildren() {
		return fields.getIdChildren();
	}
	public List<String> getIdChildren(String idField) {
		return fields.getIdChildren(idField);
	}
	@Override
	public void setProperty(String name, Object value) {
		properties.put(name, value);
		if (PARSER.equals(name)) {
			fields.fieldGroup = ((IParser)value).getFieldGroup();
		}
	}
	@Override
	public Map<String,Object> getProperties() {
		return properties;
	}
	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}
	@Override
	public Object removeProperty(String name) {
		return properties.remove(name);
	}
	@Override
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}
	@Override
	public String get(String key) {
		if (HEADER.equals(key) || MTI.equals(key)) {
			return (String)properties.get(key);
		} else {
			return getValue(key);
		}
	}
}
