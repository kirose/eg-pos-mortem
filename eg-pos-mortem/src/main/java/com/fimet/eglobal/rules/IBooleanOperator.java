package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public interface IBooleanOperator {
	Result eval(DocumentContext json);
}
