package com.fimet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fimet.IAdapterManager;
import com.fimet.parser.AdapterException;
import com.fimet.parser.IAdapter;
import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStreamAdapter;
import com.fimet.parser.IStringAdapter;
import com.fimet.parser.adapter.ExtractAdapter;
import com.fimet.parser.adapter.HexAdapter;
import com.fimet.parser.adapter.LogHexParserAdapter;
import com.fimet.parser.adapter.MliExclusiveAdapter;
import com.fimet.parser.adapter.MliExclusiveSimQueueAdapter;
import com.fimet.parser.adapter.MliInclusiveAdapter;
import com.fimet.parser.adapter.MliInclusiveSimQueueAdapter;
import com.fimet.parser.adapter.MliVisaExclusiveAdapter;
import com.fimet.parser.adapter.MliVisaExclusiveSimQueueAdapter;
import com.fimet.parser.adapter.NoneAdapter;
import com.fimet.parser.adapter.RawcomAdapter;
import com.fimet.parser.adapter.StxAdapter;
import com.fimet.parser.adapter.StxSimQueueAdapter;
@Component
public class AdapterManager implements IAdapterManager {
	
	public static final List<IAdapter> adapters = new ArrayList<>();
	public static final List<IStreamAdapter> adaptersStream = new ArrayList<>();
	public static final List<IStringAdapter> adaptersString = new ArrayList<>();
	public static final List<IByteArrayAdapter> adaptersByteArray = new ArrayList<>();
	
	public static final IAdapter STX                          = new StxAdapter(0, "Stx");
	public static final IAdapter STC_SIM_QUEUE                = new StxSimQueueAdapter(1, "StxSimQueue");
	public static final IAdapter MLI_VISA_EXCLUSIVE           = new MliVisaExclusiveAdapter(2, "VisaExclusive");
	public static final IAdapter MLI_VISA_EXCLUSIVE_SIM_QUEUE = new MliVisaExclusiveSimQueueAdapter(3, "VisaExclusiveSimQueue");
	public static final IAdapter MLI_EXCLISIVE_SIM_QUEUE      = new MliExclusiveSimQueueAdapter(4, "ExclusiveSimQueue");
	public static final IAdapter MLI_INCLUSIVE_SIM_QUEUE      = new MliInclusiveSimQueueAdapter(5, "InclusiveSimQueue");
	public static final IAdapter MLI_EXCLUSIVE                = new MliExclusiveAdapter(6, "Exclusive");
	public static final IAdapter MLI_INCLUSIVE                = new MliInclusiveAdapter(7, "Inclusive");
	public static final IAdapter HEX                          = new HexAdapter(8, "Hex");
	public static final IAdapter RAWCOM                       = new RawcomAdapter(9, "Rawcom");
	public static final IAdapter LOG_HEX                      = new LogHexParserAdapter(10, "LogHex");
	public static final IAdapter EXTRACT                      = new ExtractAdapter(11, "Extract");
	public static final IAdapter NONE                         = new NoneAdapter(12, "None");

	public AdapterManager() {
	}
	public IAdapter getAdapter(String name) {
		for (IAdapter a : adapters) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	public IAdapter getAdapter(Integer id) {
		return adapters.get(id);
	}
	public List<IAdapter> getAdapters() {
		return adapters;
	}
	public List<IStreamAdapter> getStreamAdapters() {
		List<IStreamAdapter> list = new ArrayList<>(adaptersStream.size());
		for (IStreamAdapter a : adaptersStream) {
			list.add(a);
		}
		return list;
	}
	public List<IByteArrayAdapter> getByteArrayAdapters() {
		List<IByteArrayAdapter> list = new ArrayList<>(adaptersByteArray.size());
		for (IByteArrayAdapter a : adaptersByteArray) {
			list.add(a);
		}
		return list;
	}
	public List<IStringAdapter> getStringAdapters() {
		List<IStringAdapter> list = new ArrayList<>(adaptersString.size());
		for (IStringAdapter a : adaptersString) {
			list.add(a);
		}
		return list;
	}
	public IStringAdapter adapterFor(String message) {
		for(IStringAdapter a : adaptersString) {
			if (a.isAdaptable(message)) {
				return a;
			}
		}
		throw new AdapterException("Cannot determinate adapter for "+message);
	}
	public IByteArrayAdapter adapterFor(byte[] message) {
		for(IByteArrayAdapter a : adaptersByteArray) {
			if (a.isAdaptable(message)) {
				return a;
			}
		}
		throw new AdapterException("Cannot determinate adapter for "+new String(message));
	}
	
	
	@Override
	public void start() {}
	@Override
	public void reload() {}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
