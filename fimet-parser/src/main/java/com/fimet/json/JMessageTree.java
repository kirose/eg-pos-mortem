package com.fimet.json;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fimet.parser.Field;

public class JMessageTree {
	private Map<String,String> properties;
	Map<String,Field> fields;
	public JMessageTree() {
		properties = new HashMap<String,String>();
		fields = new LinkedHashMap<String,Field>();
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public String getProperty(String name) {
		return properties.get(name);
	}
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public void setProperty(String name, String value) {
		properties.put(name, value);
	}
	public Map<String, Field> getFields() {
		return fields;
	}
	public void setFields(Map<String, Field> fields) {
		this.fields = fields;
	}
	public void remove(String idField) {
		int index = idField.indexOf('.');
		if (index == -1) {
			if (fields.containsKey(idField)) {
				fields.remove(idField);
			}
		} else {
			Field parent = null;
			Field field = null;
			String id;
			do {
				id = idField.substring(0,index);
				idField = idField.substring(index+1);
				parent = field;
				field = getChild(parent, id);
			} while (field!=null&&(index = idField.indexOf('.', index+1))!=-1);
			if (parent!=null&&field!=null) {
				parent.remove(field);
			}
		}
	}
	private Field getChild(Field parent, String idField) {
		if (parent==null) {
			if (fields.containsKey(idField)) {
				return fields.get(idField);
			}
		} else {
			Field field = parent.getField(idField);
			return field;
		}
		return null;
	}
	public void setValue(String idField, String value) {
		Field field = getFieldSafe(idField);
		field.setValue(value);
	}
	private Field getFieldSafe(String idField) {
		int index = idField.indexOf('.');
		if (index == -1) {
			return getChildSafe(null, idField);
		} else {
			Field field = null;
			String id;
			do {
				id = idField.substring(0,index);
				idField = idField.substring(index+1);
				field = getChildSafe(field, id);
			} while ((index = idField.indexOf('.', index+1))!=-1);
			return field;
		}
	}
	private Field getChildSafe(Field parent, String idField) {
		if (parent==null) {
			if (!fields.containsKey(idField)) {
				fields.put(idField, new Field());
			}
			return fields.get(idField);
		} else {
			Field field = parent.getField(idField);
			if (field==null) {
				field = new Field(idField);
				parent.add(field);
			}
			return field;
		}
	}
	public boolean hasValue(String idField) {
		return fields.containsKey(idField);
	}
	public void add(Field field) {
		fields.put(field.getIdField(), field);
	}
}
