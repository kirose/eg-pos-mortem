package com.fimet.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class Field {
	private String idField;
	private String key;
	private byte[] bytes;
	private List<Field> children;
	public Field() {
	}
	public Field(String idField) {
		this(idField, null, null);
	}
	public Field(String key, String value) {
		this(key, value!=null?value.getBytes():null);
	}
	public Field(String idField, byte[] bytes) {
		this(idField, bytes, null);
	}
	public Field(String idField, byte[] bytes, Field[] children) {
		this.idField = idField;
		this.bytes = bytes;
		this.children = children!=null?Arrays.asList(children):null;
		if (idField != null) {
			int index = idField.lastIndexOf('.');
			this.key = index != -1 ? idField.substring(index+1) : idField;
		}
	}
	public void add(Field child) {
		if (children == null) {
			children = new LinkedList<>();
		}
		children.add(child);
	}
	public void setValue(String value) {
		bytes = value!=null ? value.getBytes() : null;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getValue() {
		return bytes != null ? new String(bytes) : null;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public String getIdField() {
		return idField;
	}
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}
	public List<Field> getChildren() {
		return children;
	}
	public String getKey() {
		return key;
	}
	public Field getField(String idField) {
		if (children!=null) {
			for (Field c : children) {
				c.idField.equals(idField);
			}
		}
		return null;
	}
	public void remove(Field field) {
		if (children!=null) {
			children.remove(field);
		}
	}
}