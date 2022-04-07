package com.fimet.json;

import java.util.HashMap;
import java.util.Map;

public class JMessageMap {
	private Map<String,String> properties;
	private Map<String, String> fields;
	public JMessageMap() {
		properties = new HashMap<String,String>();
		fields = new HashMap<String,String>();
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
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	public void setValue(String idField, String value) {
		fields.put(idField, value);
	}
	public boolean hasValue(String idField) {
		return fields.containsKey(idField);
	}
}
