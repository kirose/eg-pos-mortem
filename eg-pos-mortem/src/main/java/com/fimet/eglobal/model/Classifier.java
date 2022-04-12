package com.fimet.eglobal.model;

import java.util.Map;

public class Classifier {
	private String name;
	private Map<String, Classification> classifications;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Classification> getClassifications() {
		return classifications;
	}
	public void setClassifications(Map<String, Classification> classifications) {
		this.classifications = classifications;
	}
	@Override
	public String toString() {
		return "Group (" + classifications + ")";
	}
}
