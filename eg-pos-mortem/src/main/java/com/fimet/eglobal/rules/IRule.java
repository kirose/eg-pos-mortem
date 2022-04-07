package com.fimet.eglobal.rules;

import com.fimet.parser.IMappable;

public interface IRule {
	public boolean eval(IMappable mappeable);
}
