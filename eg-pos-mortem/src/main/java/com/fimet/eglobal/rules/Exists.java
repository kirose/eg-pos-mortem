package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public class Exists implements IBooleanOperator {
	private IValueOperator arg;
	public Exists(IValueOperator arg) {
		super();
		this.arg = arg;
	}
	@Override
	public Result eval(DocumentContext json) {
		return new Result(arg.eval(json) != null);
	}
	@Override
	public String toString() {
		return "[existe][" + arg+ "]";
	}
}
