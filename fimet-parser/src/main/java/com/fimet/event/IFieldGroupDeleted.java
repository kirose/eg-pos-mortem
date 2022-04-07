package com.fimet.event;

import com.fimet.parser.IEFieldGroup;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldGroupDeleted extends IEventListener {
	public void onFieldGroupDeleted(IEFieldGroup group);
}
