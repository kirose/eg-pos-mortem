package com.fimet.eglobal.model;

import java.util.Date;

import javax.persistence.*;

@Entity(name="TRANSACTION_LOG")
@IdClass(TransactionLogId.class)
public class TransactionLog {
	@Id
	@Column(name = "TRL_ID")
	private Long id;
	@Id
	@Column(name = "TRL_SYSTEM_TIMESTAMP")
	private Date timestamp;
	@Column(name = "TRL_RRN")
	private String rrn;
	@Column(name = "TRL_ORIGIN_IAP_NAME")
	private String srcIap;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getSrcIap() {
		return srcIap;
	}
	public void setSrcIap(String srcIap) {
		this.srcIap = srcIap;
	}
}
