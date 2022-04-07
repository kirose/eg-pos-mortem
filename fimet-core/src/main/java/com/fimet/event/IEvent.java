package com.fimet.event;

public interface IEvent {
	Object getType();
	Object getSource();
	Object[] getParams();
}
