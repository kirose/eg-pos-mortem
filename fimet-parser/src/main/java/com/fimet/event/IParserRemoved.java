package com.fimet.event;

import com.fimet.parser.IParser;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IParserRemoved extends IEventListener {
	public void onParserRemoved(IParser parser);
}
