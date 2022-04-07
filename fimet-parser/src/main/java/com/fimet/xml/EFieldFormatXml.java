package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.IEFieldFormat;

/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
@XmlRootElement(name="field")
@XmlAccessorType(XmlAccessType.NONE)
public class EFieldFormatXml implements IEFieldFormat {
	private String group;
	@XmlAttribute(name="id")
	private String idField;// 63.EZ
	private String idFieldParent;// 063
	@XmlAttribute(name="converterValue")
	private String converterValue;// NONE, HEX_TO_ASCII, ASCII_TO_HEX, EBCDIC_TO_ASCII
	@XmlAttribute(name="converterLength")
	private String converterLength;// HEX_TO_ASCII, ASCII_TO_HEX, EBCDIC_TO_ASCII
	@XmlAttribute(name="parserLength")
	private String parserLength;// Hex, HexDouble, Decimal
	@XmlAttribute(name="order")
	private String order;// 063.16, se utiliza para visualizar en Preferences->Parsers y para generar address de un field
	private String childs;//1,2,3,4,5,6,7,8,9,10,11,12
	@XmlAttribute(name="mask")
	private String mask;// [A-Z]+
	@XmlAttribute(name="length")
	private Integer length;// 12, 1 numero de bytes que debe de tomar del padre
	@XmlAttribute(name="maxLength")
	private Integer maxLength;// Para campos variable
	@XmlAttribute(name="name")
	private String name;// Token EZ
	@XmlAttribute(name="parser")
	private String classParser;	// com.fimet.fmt.impl.FieldParser
	@XmlElement(name="field")
	private List<EFieldFormatXml> children;
	public EFieldFormatXml() {}
	public EFieldFormatXml(String group, String order, String idField, String converterValue, String converterLength, String parserLength, String type, Integer length, Integer maxLength, String name, Class<?> classParser) {
		super();
		this.group = group;
		this.order = order;
		this.idField = idField;
		this.converterValue = converterValue;
		this.converterLength = converterLength;
		this.parserLength = parserLength;
		this.mask = type;
		this.length = length;
		this.maxLength = maxLength;
		this.name = name;
		this.classParser = classParser.getName();
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getIdFieldParent() {
		return idFieldParent;
	}
	public void setIdFieldParent(String idFieldParent) {
		this.idFieldParent = idFieldParent;
	}
	public String getChilds() {
		return childs;
	}
	public void setChilds(String childs) {
		this.childs = childs;
	}
	public String getConverterValue() {
		return converterValue;
	}
	public void setConverterValue(String converterValue) {
		this.converterValue = converterValue;
	}
	public String getConverterLength() {
		return converterLength;
	}
	public void setConverterLength(String converterLength) {
		this.converterLength = converterLength;
	}
	public String getParserLength() {
		return parserLength;
	}
	public void setParserLength(String parserLength) {
		this.parserLength = parserLength;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String type) {
		this.mask = type;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassParser() {
		return classParser;
	}
	public void setClassParser(String classParser) {
		this.classParser = classParser;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public List<EFieldFormatXml> getChildren() {
		return children;
	}
	public void setChildren(List<EFieldFormatXml> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "[order="+order+", idField=" + idField+ ", idFieldParent=" + idFieldParent + ", childs="
				+ childs + ", mask=" + mask + ", length=" + length
				+ ", name=" + name + ", classParser=" + classParser + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EFieldFormatXml other = (EFieldFormatXml) obj;
		/*if (idFieldFormat != null && other.idFieldFormat != null && idFieldFormat == other.idFieldFormat) {
			return true;
		}*/
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}
}
