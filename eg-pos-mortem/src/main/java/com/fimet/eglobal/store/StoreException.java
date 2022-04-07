package com.fimet.eglobal.store;

public class StoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StoreException() {
		super();
	}

	public StoreException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public StoreException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public StoreException(String arg0) {
		super(arg0);
	}

	public StoreException(Throwable arg0) {
		super(arg0);
	}

}
