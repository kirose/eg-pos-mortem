package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEParser;

public interface IParserDAO<T extends IEParser> extends IManager {
	public T findByName(String name);
	public List<T> findAll();
	public T insert(T parser);
	public T update(T parser);
	public T delete(T parser);
}
