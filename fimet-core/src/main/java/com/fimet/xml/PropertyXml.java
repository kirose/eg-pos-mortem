package com.fimet.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.NONE)
public class PropertyXml {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="value")
	private String value;
	public PropertyXml() {
		super();
	}
	public PropertyXml(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
