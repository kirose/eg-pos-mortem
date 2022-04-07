package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEFieldFormat;

public interface IFieldFormatDAO<T extends IEFieldFormat> extends IManager {
	public List<T> findByGroup(String group);
	public List<T> findAll();
	public T insert(T format);
	public T update(T format);
	public T delete(T format);
}
