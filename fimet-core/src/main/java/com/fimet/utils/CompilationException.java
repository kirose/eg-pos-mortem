package com.fimet.utils;

import com.fimet.FimetException;

public class CompilationException extends FimetException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public CompilationException() {
	}

	public CompilationException(String message) {
		super(message);
	}

	public CompilationException(Throwable cause) {
		super(cause);
	}

	public CompilationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompilationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
