package com.fimet.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;
import com.fimet.parser.IEParser;
import com.fimet.parser.IFieldGroup;
import com.fimet.parser.IParser;
import com.fimet.parser.ParserException;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.xml.EParserXml;

public class ParserBuilder {
	private EParserXml entity;
	private Class<?> parserClass;

	public ParserBuilder() {
		super();
		entity= new EParserXml();
		entity.setFieldGroup("National");
		entity.setConverter(Converter.NONE.toString());
	}
	public ParserBuilder name(String name) {
		entity.setName(name);
		return this;
	}
	public ParserBuilder fieldGroup(String name) {
		IFieldGroup group = Manager.getManager(IFieldGroupManager.class).getGroup(name);
		if (group == null) {
			throw new FimetException("Unkown field group "+name);
		}
		entity.setFieldGroup(group.getName());
		return this;
	}
	public ParserBuilder converter(IConverter converter) {
		entity.setConverter(converter.getName());
		return this;
	}
	public ParserBuilder parserClass(Class<? extends IParser> parserClass) {
		this.parserClass = parserClass;
		entity.setParserClass(parserClass.getName());
		return this;
	}
	public IParser build() {
		if (parserClass == null) {
			throw new ParserException("Parser class cannot be null");
		}
		if (entity.getName() == null) {
			entity.setName(parserClass.getSimpleName());
		}
		return newParser(entity);
	}
	private IParser newParser(IEParser entity) {
		try {
			Constructor<?> constructor = parserClass.getConstructor(com.fimet.parser.IEParser.class);
			return (IParser) constructor.newInstance(entity);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (NoSuchMethodException e) {
			throw new ParserException("No found public constructor with "+IEParser.class.getName()+" as argument in class: " + entity.getParserClass(),e);
		} catch (SecurityException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		}
	}
}
