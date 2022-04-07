package com.fimet.eglobal.model;

import java.util.Map;

public class OperativeGroup {
	private Map<String, Operative> operatives;
	public Map<String, Operative> getOperatives() {
		return operatives;
	}
	public void setOperatives(Map<String, Operative> operatives) {
		this.operatives = operatives;
	}
	@Override
	public String toString() {
		return "Group (" + operatives + ")";
	}
}
