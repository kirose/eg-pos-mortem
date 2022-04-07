package com.fimet.parser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IMessage extends Cloneable, IMappable {

	public static final String USECASE = "usecase";
	public static final String SESSION = "session";
	public static final String PARSER = "parser";
	public static final String ADAPTER = "adapter";
	public static final String HEADER = "header";
	public static final String MTI = "mti";
	public static final String BDK = "bdk";
	
	public IParser getParser();
	public void setParser(IParser adapter);
	public void setProperty(String name, Object value);
	public boolean hasProperty(String name);
	public Object getProperty(String name);
	public Object removeProperty(String name);
	public Map<String,Object> getProperties();

	public String getValue(int idField);
	public String getValue(String idField);
	public byte[] getValueAsBytes(int idField);
	public byte[] getValueAsBytes(String idField);
	public void setValue(int idField, String value);
	public void setValue(String idField, String value);
	public void setValue(int idField, byte[] value);
	public void setValue(String idField, byte[] value);
	public boolean hasField(String idField);
	public boolean hasField(int idField);
	public void remove(int idField);
	public void remove(String idField);
	public void removeAll(String ... idFields);
	public void removeAll(Collection<String> idFields);

	public boolean hasChildren(String idField);
	public List<String> getIdChildren(String idField);
	public List<Field> getRootsAsList();
	public Map<String,Field> getRootsAsMap();
	public IMessage clone() throws CloneNotSupportedException;
}
