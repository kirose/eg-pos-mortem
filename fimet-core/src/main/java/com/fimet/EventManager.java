package com.fimet;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.event.*;
import com.fimet.FimetException;

@Component
public class EventManager implements IEventManager {
	private static Logger logger = LoggerFactory.getLogger(EventManager.class);
	@Autowired private Manager manager;
	private Map<Object, ConcurrentLinkedQueue<IEventListener>> table = new HashMap<Object, ConcurrentLinkedQueue<IEventListener>>();
	private Map<Object, Integer> mapContributors = new HashMap<Object, Integer>();
	private IEventContributor[] contributors;
	public EventManager() {}
	@PostConstruct
	@Override
	public void start() {
		reload();
	}

	@Override
	public void reload() {
		List<IEventContributor> contributors = manager.getExtensions(IEventContributor.class);
		if (contributors!=null&&!contributors.isEmpty()) {
			int indexContributor = 0;
			this.contributors = new IEventContributor[contributors.size()];
			for (IEventContributor c : contributors) {
				this.contributors[indexContributor] = c;
				Object[] eventTypes = c.getEventTypes();
				if (eventTypes!=null && eventTypes.length > 0) {
					for (Object eventType : eventTypes) {
						mapContributors.put(eventType, indexContributor);
					}
				}
				indexContributor++;
			}
		}
	}

	@Override
	public void fireEvent(Object type, Object source, Object ...params) {
		fireEvent(new Event(type, source, params));
	}

	@Override
	public void fireEvent(IEvent event) {
		if (table.containsKey(event.getType())) {
			ConcurrentLinkedQueue<IEventListener> listners = table.get(event.getType());
			if (listners!=null && !listners.isEmpty()) {
				if (mapContributors.containsKey(event.getType())) {
					Integer indexContributor = mapContributors.get(event.getType());
					IEventContributor contributor = contributors[indexContributor];
					for (IEventListener l : listners) {
						try {
							contributor.fireEvent(event, l);
						} catch (Throwable e) {
							logger.error("Fire Envent Error", e);
						}
					}
				} else {
					throw new FimetException("Invalid Event "+event);
				}
			}
		}
	}

	@Override
	public void addListener(Object type, IEventListener listener) {
		if (!table.containsKey(type)) {
			table.put(type, new ConcurrentLinkedQueue<IEventListener>());
		}
		table.get(type).add(listener);
	}

	@Override
	public void removeListener(Object type, IEventListener listener) {
		if (table.containsKey(type)) {
			table.get(type).remove(listener);
		}
	}
	@Override
	public void stop() {}
}
