package com.fimet.event;

import com.fimet.parser.IEParser;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IParserUpdated extends IEventListener {
	public void onParserUpdated(IEParser parser);
}
