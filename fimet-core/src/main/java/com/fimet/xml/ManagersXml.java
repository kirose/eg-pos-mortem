package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="managers")
@XmlAccessorType(XmlAccessType.NONE)
public class ManagersXml {
	@XmlElement(name="manager")
	private List<ManagerXml> managers;
	public ManagersXml() {
		super();
	}
	public ManagersXml(List<ManagerXml> managers) {
		super();
		this.managers = managers;
	}
	public List<ManagerXml> getManagers() {
		return managers;
	}
	public void setManagers(List<ManagerXml> managers) {
		this.managers = managers;
	}
}
