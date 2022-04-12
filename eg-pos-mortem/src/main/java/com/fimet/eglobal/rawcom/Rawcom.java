package com.fimet.eglobal.rawcom;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fimet.parser.IMessage;

public class Rawcom {
	private Date time;
	private Integer dispatcher;
	private String pan;
	private String iap;
	private String listProcessor;
	private char direction;
	private int length;
	private byte[] isoMessage;
	private int mti;
	private RawcomType type;
	private IMessage message;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getDispatcher() {
		return dispatcher;
	}
	public void setDispatcher(Integer dispatcher) {
		this.dispatcher = dispatcher;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getIap() {
		return iap;
	}
	public void setIap(String iap) {
		this.iap = iap;
	}
	public String getListProcessor() {
		return listProcessor;
	}
	public void setListProcessor(String listProcessor) {
		this.listProcessor = listProcessor;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public IMessage getMessage() {
		return message;
	}
	public void setMessage(IMessage message) {
		this.message = message;
	}
	public byte[] getIsoMessage() {
		return isoMessage;
	}
	public void setIsoMessage(byte[] isoMessage) {
		this.isoMessage = isoMessage;
	}
	public RawcomType getType() {
		return type;
	}
	public void setType(RawcomType type) {
		this.type = type;
	}
	public char getDirection() {
		return direction;
	}
	public void setDirection(char direction) {
		this.direction = direction;
	}
	public int getMti() {
		return mti;
	}
	public void setMti(int mti) {
		this.mti = mti;
	}
	public String toString() {
		return "[T: "+new SimpleDateFormat("HH:mm:ss.SSS").format(time)+"][D: "+dispatcher+"][C: "+pan+"][Iap: "+iap+"][Lp: "+listProcessor+"][Rw: "+direction+"][L: "+length+"]"; 
	}
}
