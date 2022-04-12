package com.fimet.eglobal.classification;

import com.jayway.jsonpath.DocumentContext;

public interface IRule {
	public boolean eval(DocumentContext json);
}
