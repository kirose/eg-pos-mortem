package com.fimet.eglobal.rules;

import static com.fimet.eglobal.utils.StringUtils.prettyJPath;

import com.jayway.jsonpath.DocumentContext;

public class Equals implements IBooleanOperator {
	private IValueOperator left;
	private IValueOperator right;
	public Equals(IValueOperator left, IValueOperator right) {
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
		return "[igual][" + prettyJPath(left.toString()) + "][" + prettyJPath(right.toString()) + "]";
	}
}
