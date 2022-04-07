package com.fimet.event;

import com.fimet.IExtension;

public interface IEventContributor extends IExtension {
	Object[] getEventTypes();
	void fireEvent(IEvent event, IEventListener listener);
}
