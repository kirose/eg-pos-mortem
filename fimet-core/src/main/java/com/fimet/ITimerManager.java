package com.fimet;

import com.fimet.utils.ITimerListener;
import com.fimet.utils.Scheduled;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ITimerManager extends IManager {
	Scheduled schedule(Object object, long time, ITimerListener listener);
	void cancel(Object object);
}
