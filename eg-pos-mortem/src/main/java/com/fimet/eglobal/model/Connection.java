package com.fimet.eglobal.model;

import com.fimet.parser.IParser;

public class Connection {
	public enum Type {
		ACQUIRER, ISSUER, SERVICE
	}
	private Type type;
	private String alias;
	private IParser parser;
	private boolean sanitizeHex;
	private Classifier classifier;
	public Connection() {
		super();
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public IParser getParser() {
		return parser;
	}
	public void setParser(IParser parser) {
		this.parser = parser;
	}
	public boolean isSanitizeHex() {
		return sanitizeHex;
	}
	public void setSanitizeHex(boolean sanitizeHex) {
		this.sanitizeHex = sanitizeHex;
	}
	public Classifier getClassifier() {
		return classifier;
	}
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}
}
