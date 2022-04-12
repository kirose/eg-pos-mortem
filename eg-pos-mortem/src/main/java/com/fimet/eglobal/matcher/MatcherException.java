package com.fimet.eglobal.matcher;

public class MatcherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatcherException() {
		super();
	}

	public MatcherException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MatcherException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MatcherException(String arg0) {
		super(arg0);
	}

	public MatcherException(Throwable arg0) {
		super(arg0);
	}

}
