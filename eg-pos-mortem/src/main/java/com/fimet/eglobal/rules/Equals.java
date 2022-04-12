package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public class Equals implements IBooleanOperator {
	private IStringOperator left;
	private IStringOperator right;
	public Equals(IStringOperator left, IStringOperator right) {
		super();
		this.left = left;
		this.right = right;
	}
	@Override
	public Result eval(DocumentContext json) {
		String l = left.eval(json);
		String r = right.eval(json);
		if (l == null) {
			return new Result(r == null, new String[] {l, r});
		} else {
			return new Result(l.equals(r), new String[] {l, r});
		}
	}
	@Override
	public String toString() {
		return "equals(" + left + "," + right + ")";
	}
}
