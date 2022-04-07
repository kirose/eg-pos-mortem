package com.fimet.eglobal.rawcom;

public enum RawcomType {
	NETWORK_REQUEST,
	NETWORK_RESPONSE,
	AUTHORIZATION_REQUEST,
	AUTHORIZATION_RESPONSE,
	REVERSAL_REQUEST,
	REVERSAL_RESPONSE;
	public static RawcomType get(String mti) {
		if ("0800".equals(mti)) {
			return NETWORK_REQUEST;
		}
		if ("0810".equals(mti)) {
			return NETWORK_RESPONSE;
		}
		if ("0100".equals(mti) || "0200".equals(mti) || "0220".equals(mti)
				|| "0101".equals(mti) || "0201".equals(mti) || "0221".equals(mti)) {
			return AUTHORIZATION_REQUEST;
		}
		if ("0110".equals(mti) || "0210".equals(mti) || "0230".equals(mti)) {
			return AUTHORIZATION_RESPONSE;
		}
		if ("0420".equals(mti) || "0421".equals(mti) || "0400".equals(mti) || "0401".equals(mti)) {
			return REVERSAL_REQUEST;
		}
		if ("0430".equals(mti) || "0410".equals(mti)) {
			return REVERSAL_RESPONSE;
		}
		throw new RuntimeException("Unknow mti "+mti);
	}
	public boolean isRequest() {
		return this == NETWORK_REQUEST || this == AUTHORIZATION_REQUEST || this == REVERSAL_REQUEST;
	}
	public boolean isResponse() {
		return this == NETWORK_RESPONSE || this == AUTHORIZATION_RESPONSE || this == REVERSAL_RESPONSE;
	}
}
