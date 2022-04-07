package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="extension")
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionXml {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="class")
	private String className;
	public ExtensionXml() {
		super();
	}
	public ExtensionXml(String id, String className) {
		super();
		this.name = id;
		this.className = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String id) {
		this.name = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String toString() {
		return name;
	}
}
