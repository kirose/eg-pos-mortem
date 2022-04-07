package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="database")
@XmlAccessorType(XmlAccessType.NONE)
public class DatabaseXml {
	@XmlAttribute(name="name")
	private String id;
	@XmlAttribute(name="path")
	private String path;
	@XmlAttribute(name="creator")
	private String creator;
	public DatabaseXml() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
}
