package com.fimet.eglobal.rules;

import com.jayway.jsonpath.DocumentContext;

public interface IValueOperator {
	<T>T eval(DocumentContext json);
}
