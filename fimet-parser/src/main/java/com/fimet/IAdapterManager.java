package com.fimet;

import java.util.List;

import com.fimet.parser.IAdapter;
import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStreamAdapter;
import com.fimet.parser.IStringAdapter;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IAdapterManager extends IManager {
	
	IAdapter getAdapter(String name);
	IAdapter getAdapter(Integer id);
	List<IAdapter> getAdapters();
	List<IStreamAdapter> getStreamAdapters();
	List<IByteArrayAdapter> getByteArrayAdapters();
	List<IStringAdapter> getStringAdapters();
	IStringAdapter adapterFor(String message);
	IByteArrayAdapter adapterFor(byte[] message);

}
