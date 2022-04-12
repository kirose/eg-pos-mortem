package com.fimet.eglobal.model;

import java.util.List;

import com.fimet.eglobal.classification.IRule;

public class Classification {
	private String name;
	private List<IRule> rules;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<IRule> getRules() {
		return rules;
	}
	public void setRules(List<IRule> rules) {
		this.rules = rules;
	}
	@Override
	public String toString() {
		return "Rules(" + rules + ")";
	}
}
