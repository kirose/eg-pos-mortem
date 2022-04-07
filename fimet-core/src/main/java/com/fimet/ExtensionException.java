package com.fimet;

public class ExtensionException extends FimetException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtensionException() {
	}

	public ExtensionException(String message) {
		super(message);
	}

	public ExtensionException(Throwable cause) {
		super(cause);
	}

	public ExtensionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
