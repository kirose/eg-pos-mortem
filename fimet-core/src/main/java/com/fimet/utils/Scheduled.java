package com.fimet.utils;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.fimet.FimetException;
import com.fimet.ITimerManager;
import com.fimet.Manager;

public class Scheduled implements Delayed {
	static ITimerManager timerManager = Manager.getManager(ITimerManager.class);
	public static final long MIN_WAITING_TIME = Manager.getPropertyLong("timerManager.minWaitingTime",5L);
	public static final long MAX_WAITING_TIME = Manager.getPropertyLong("timerManager.maxWaitingTime",1000L*60);
	long timeout;
	Object object;
	ITimerListener listener;
	public Scheduled(Object object, long time, ITimerListener listener) {
		this.listener = listener;
		this.object = object;
		if (time < MIN_WAITING_TIME) {
			throw new FimetException("Invalid time "+time+", min allowed:"+MIN_WAITING_TIME);
		}
		if (time > MAX_WAITING_TIME) {
			throw new FimetException("Invalid time "+time+", max allowed:"+MAX_WAITING_TIME);
		}
		this.timeout = System.currentTimeMillis() + time;
	}
	public void cancel() {
		timerManager.cancel(this);
	}
	public Object getObject() {
		return object;
	}
	public ITimerListener getListener() {
		return listener;
	}
	@Override
	public int compareTo(Delayed o) {
		long other = ((Scheduled)o).timeout;
		return timeout < other ? -1 : (timeout == other ? 0 : 1);
	}
	@Override
	public long getDelay(TimeUnit unit) {
        return unit.convert(timeout-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
}