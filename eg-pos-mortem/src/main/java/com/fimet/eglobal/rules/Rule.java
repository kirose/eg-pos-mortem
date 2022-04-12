package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public class Rule {
	private String name;
	private IBooleanOperator operator;
	public Rule() {}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IBooleanOperator getOperator() {
		return operator;
	}
	public void setOperator(IBooleanOperator operator) {
		this.operator = operator;
	}
	public Result eval(DocumentContext json) {
		return operator.eval(json);
	}
	@Override
	public String toString() {
		return name + ":" + operator;
	}
}
