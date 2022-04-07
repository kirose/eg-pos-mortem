package com.fimet.eglobal.desc;

import java.util.Date;
import java.util.List;

import com.fimet.parser.IMessage;

public class Desc {
	private String line;
	private IMessage message;
	private Long sequence;
	private List<IMessage> additionals;
	private Date time;
	public Desc() {
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public IMessage getMessage() {
		return message;
	}
	public void setMessage(IMessage message) {
		this.message = message;
	}
	public List<IMessage> getAdditionals() {
		return additionals;
	}
	public void setAdditionals(List<IMessage> additionals) {
		this.additionals = additionals;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getTime() {
		return time;
	}
	@Override
	public String toString() {
		return "Desc [time=" + time + "]";
	}
}
