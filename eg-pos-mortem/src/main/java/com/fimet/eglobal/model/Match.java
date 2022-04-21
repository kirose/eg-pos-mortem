package com.fimet.eglobal.model;

import java.util.List;

import com.jayway.jsonpath.DocumentContext;

public class Match {
	private DocumentContext json;
	private Classifier classifier;
	private List<Classification> classifications;
	public DocumentContext getJson() {
		return json;
	}
	public void setJson(DocumentContext json) {
		this.json = json;
	}
	public Classifier getClassifier() {
		return classifier;
	}
	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}
	public List<Classification> getClassifications() {
		return classifications;
	}
	public void setClassifications(List<Classification> classifications) {
		this.classifications = classifications;
	}
}
