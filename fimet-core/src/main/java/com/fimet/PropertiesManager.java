package com.fimet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesManager implements IPropertiesManager {
	
	@Autowired private Environment env;
	
	private Map<String, String> properties = new HashMap<String, String>();
	public PropertiesManager(){}
	
	@Override
	public Boolean getBoolean(String name) {
		String value = env.getProperty(name);
		return value != null ? Boolean.valueOf(value) : null;
	}

	@Override
	public Boolean getBoolean(String name, boolean defaultValue) {
		String value = env.getProperty(name);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
	
	@Override
	public Integer getInteger(String name) {
		String value = env.getProperty(name);
		return value != null ? Integer.valueOf(value) : null;
	}

	@Override
	public Integer getInteger(String name, int defaultValue) {
		String value = env.getProperty(name);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}
	
	@Override
	public Long getLong(String name) {
		String value = env.getProperty(name);
		return value != null ? Long.valueOf(value) : null;
	}

	@Override
	public Long getLong(String name, long defaultValue) {
		String value = env.getProperty(name);
		return value != null ? Long.valueOf(value) : defaultValue;
	}

	
	@Override
	public String getString(String name) {
		return env.getProperty(name);
	}

	@Override
	public String getString(String name, String defaultValue) {
		String value = env.getProperty(name);
		return value != null ? value: defaultValue;
	}

	
	@Override
	public Double getDouble(String name) {
		String value = env.getProperty(name);
		return value != null ? Double.valueOf(value) : null;
	}

	@Override
	public Double getDouble(String name, double defaultValue) {
		String value = env.getProperty(name);
		return value != null ? Double.valueOf(value) : defaultValue;
	}
	@Override
	public void setValue(String key, String value) {
		properties.put(key, value);
	}
	@Override
	public void removePropery(String key) {
		properties.remove(key);
	}
}
