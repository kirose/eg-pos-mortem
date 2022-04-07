package com.fimet.eglobal.model;

public class Response {
	public enum Status {
		OK, ERROR
	}
	public static final Response ERROR = new Response(Status.ERROR);
	public static final Response OK = new Response(Status.OK);
	private Status status;
	private String message;
	private Object data;
	public Response() {
	}
	public Response(Status status) {
		super();
		this.status = status;
	}
	public Response(Status status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}
	public static Response newError(String message) {
		Response r = new Response(Status.ERROR);
		r.setMessage(message);
		return r;
	}
	public static Response newOk(Object data) {
		Response r = new Response(Status.OK);
		r.setData(data);
		return r;
	}
	public static Response newOk() {
		Response r = new Response(Status.OK);
		return r;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
