package com.fimet;

import com.fimet.IManager;
import com.fimet.event.IEvent;
import com.fimet.event.IEventListener;

public interface IEventManager extends IManager {
	void fireEvent(Object eventType, Object source, Object ... params);
	void fireEvent(IEvent event);
	void addListener(Object eventType, IEventListener listener);
	void removeListener(Object eventType, IEventListener listener);
}
