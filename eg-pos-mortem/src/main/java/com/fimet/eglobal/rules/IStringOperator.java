package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public interface IStringOperator {
	String eval(DocumentContext json);
}
