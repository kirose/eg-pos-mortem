package com.fimet;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IThreadManager extends IManager {
	void execute(Runnable runnable);
}
