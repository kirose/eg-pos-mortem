package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEFieldGroup;

public interface IFieldGroupDAO<T extends IEFieldGroup> extends IManager {
	public T findByName(String name);
	public List<T> findAll();
	public T insert(T group);
	public T update(T group);
	public T delete(T group);
}
