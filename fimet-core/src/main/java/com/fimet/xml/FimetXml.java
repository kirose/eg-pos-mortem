package com.fimet.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import com.fimet.utils.XmlUtils;

@XmlRootElement(name = "fimet")
@XmlAccessorType(XmlAccessType.NONE)
public class FimetXml {
	@XmlAttribute(name="local")
	private String local;
	@XmlAttribute(name="URI")
	private String URI;
	@XmlAttribute(name="xmlns")
	private String xmlns;
	@XmlAttribute(name="xmlns:xsi")
	private String xmlnsXsi;
	@XmlAttribute(name="xsi:schemaLocation")
	private String schemaLocation;
	private Map<QName,Object> attrs;
	private List<DatabaseXml> databases;
	private List<ManagerXml> managers;
	private List<PropertyXml> properties;
	private List<ExtensionXml> extensions;
	public FimetXml() {}
	@XmlAnyAttribute
	public Map<QName,Object> getAny() {
	  return attrs;
	}
	@XmlElementWrapper(name = "databases")
	@XmlElement(name = "database")
	public List<DatabaseXml> getDatabases() {
		return databases;
	}
	public void setDatabases(List<DatabaseXml> databases) {
		this.databases = databases;
	}
	@XmlElementWrapper(name = "properties")
	@XmlElement(name="property")
	public List<PropertyXml> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyXml> properties) {
		this.properties = properties;
	}
	@XmlElementWrapper(name = "managers")
	@XmlElement(name = "manager")
	public List<ManagerXml> getManagers() {
		return managers;
	}
	public void setManagers(List<ManagerXml> managers) {
		this.managers = managers;
	}
	public Map<String, String> getPropertiesMap(){
		Map<String,String> map = new HashMap<String, String>();
		if (properties != null && !properties.isEmpty()) {
			for (PropertyXml p : properties) {
				map.put(p.getName(), p.getValue());
			}
		}
		return map;
	}
	@XmlElementWrapper(name = "extensions")
	@XmlElement(name="extension")
	public List<ExtensionXml> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<ExtensionXml> extensions) {
		this.extensions = extensions;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getXmlnsXsi() {
		return xmlnsXsi;
	}
	public void setXmlnsXsi(String xmlnsXsi) {
		this.xmlnsXsi = xmlnsXsi;
	}
	public String getSchemaLocation() {
		return schemaLocation;
	}
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	public Map<QName, Object> getAttrs() {
		return attrs;
	}
	public String toString() {
		return XmlUtils.toXml(this, FimetXml.class);
	}
}
