package com.fimet.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.IEParser;

/**
 * 
 * @author Marco A. Salazar
 *
 */
@XmlRootElement(name="parser")
@XmlAccessorType(XmlAccessType.NONE)
public class EParserXml implements IEParser {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="class")
	private String parserClass;
	/**
	 * MTI+Header+Bitmap+Fields convert
	 */
	@XmlAttribute(name="converter")
	private String converter;
	@XmlAttribute(name="fieldGroup")
	private String fieldGroup;
	public EParserXml() {}
	public EParserXml(String name,
			String fieldGroup,
			String parserClass,
			String converter) {
		super();
		this.fieldGroup = fieldGroup;
		this.name = name;
		this.parserClass = parserClass;
		this.converter = converter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFieldGroup(String fieldGroup) {
		this.fieldGroup = fieldGroup;
	}
	public String getFieldGroup() {
		return fieldGroup;
	}
	public String getConverter() {
		return converter;
	}
	public void setConverter(String converter) {
		this.converter = converter;
	}
	public String getParserClass() {
		return parserClass;
	}
	public void setParserClass(String parserClass) {
		this.parserClass = parserClass;
	}
	@Override
	public String toString() {
		return name;
	}
}
