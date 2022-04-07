package com.fimet;

import com.fimet.parser.IFieldGroup;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldGroupManager extends IManager {
	IFieldGroup getGroup(String name);
	IFieldGroup reload(String name);
	IFieldGroup remove(String name);
	public Class<?>[] getParserClasses();
}
