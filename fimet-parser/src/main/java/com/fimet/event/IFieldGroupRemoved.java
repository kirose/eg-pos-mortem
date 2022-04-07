package com.fimet.event;

import com.fimet.parser.IFieldGroup;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldGroupRemoved extends IEventListener {
	public void onFieldGroupRemoved(IFieldGroup group);
}
