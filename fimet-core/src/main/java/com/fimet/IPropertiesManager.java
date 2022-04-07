package com.fimet;

public interface IPropertiesManager {
	Boolean getBoolean(String name);
	Boolean getBoolean(String name, boolean defaultValue);
	Integer getInteger(String name);
	Integer getInteger(String name, int defaultValue);
	Long getLong(String name);
	Long getLong(String name, long defaultValue);
	String getString(String name);
	String getString(String name, String defaultValue);
	Double getDouble(String name);
	Double getDouble(String name, double defaultValue);
	void setValue(String key, String value);
	void removePropery(String key);
}
