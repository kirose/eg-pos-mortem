package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public class Exists implements IBooleanOperator {
	private IStringOperator arg;
	public Exists(IStringOperator arg) {
		super();
		this.arg = arg;
	}
	@Override
	public Result eval(DocumentContext json) {
		return new Result(arg.eval(json) != null);
	}
	@Override
	public String toString() {
		return "exists(" + arg + ")";
	}
}
