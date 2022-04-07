package com.fimet.event;

public class Event implements IEvent {

	public static final int SOCKET_REMOVED = 8;
	public static final int SOCKET_LOADED = 9;
	public static final int SOCKET_CONNECTED = 10;
	public static final int SOCKET_CONNECTING = 11;
	public static final int SOCKET_DISCONNECTED = 12;
	public static final int ENVIROMENT_CONNECTED = 15;
	public static final int ENVIROMENT_CONNECTING = 16;
	public static final int ENVIROMENT_DISCONNECTED = 17;
	
	private Object type;
	private Object source;
	private Object[] params;

	public Event(Object type, Object source, Object ...params) {
		super();
		this.type = type;
		this.source = source;
		this.params = params;
	}

	@Override
	public Object getType() {
		return type;
	}
	@Override
	public Object getSource() {
		return source;
	}
	@Override
	public Object[] getParams() {
		return params;
	}
}
