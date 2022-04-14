package com.fimet.eglobal.model;

import java.util.List;

public class Validations {
	private long key;
	private String classifier;
	private List<String> classifications;
	private List<Validation> validations;
	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
	public String getClassifier() {
		return classifier;
	}
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	public List<String> getClassifications() {
		return classifications;
	}
	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}
	public List<Validation> getValidations() {
		return validations;
	}
	public void setValidations(List<Validation> validations) {
		this.validations = validations;
	} 
}
 