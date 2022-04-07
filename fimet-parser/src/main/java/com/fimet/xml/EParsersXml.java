package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="parsers")
@XmlAccessorType(XmlAccessType.NONE)
public class EParsersXml {
	@XmlElement(name="parser")
	private List<EParserXml> parsers;
	public EParsersXml() {
		super();
	}
	public EParsersXml(List<EParserXml> parsers) {
		super();
		this.parsers = parsers;
	}
	public List<EParserXml> getParsers() {
		return parsers;
	}
	public void setParsers(List<EParserXml> parsers) {
		this.parsers = parsers;
	}
}
