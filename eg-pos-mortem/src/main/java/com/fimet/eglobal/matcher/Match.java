package com.fimet.eglobal.matcher;

import java.util.ArrayList;
import java.util.List;

public class Match {
	private String rawcom;
	private String desc;
	private List<String> classifications;
	public Match(String rawcom) {
		this(rawcom, null);
	}
	public Match(String rawcom, String desc) {
		super();
		this.rawcom = rawcom;
		this.desc = desc;
		this.classifications = new ArrayList<String>();
	}
	public String getRawcom() {
		return rawcom;
	}
	public void setRawcom(String rawcom) {
		this.rawcom = rawcom;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getClassifications() {
		return classifications;
	}
	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}
	@Override
	public String toString() {
		return "Match [rawcom=" + rawcom + ", desc=" + desc + ", classifications=" + classifications
				+ "]";
	}
}
