package com.fimet.eglobal.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.rawcom.SortedList;
import com.fimet.eglobal.store.Store;


public class DescRequest {
	private static Logger logger = LoggerFactory.getLogger(DescRequest.class);
	private static final Comparator<Desc> COMPARATOR = (l, r)-> {
		return l.getSequence().compareTo(r.getSequence());
	};
	private Date start;
	private Date end;
	private long index = 0;
	private File fileIndex;
	private File fileData;
	private File fileBase;
	private File fileAdditional;
	private Map<Long, List<String>> additionalsCache;
	private SortedList<Desc> descQueue;
	private Store store;
	private DescReader readerBase;
	private BufferedReader readerAdditional;
	private int cacheSize;
	Map<String, Connection> mapConnections;
	public DescRequest(Date start, Date end, File fileBase, File fileAdditional, int cacheSize) throws IOException {
		this.start = start;
		this.end = end;
		this.cacheSize = cacheSize;
		this.fileBase = fileBase;
		this.fileAdditional = fileAdditional;
		readerBase = new DescReader(fileBase);
		readerAdditional = new BufferedReader(new FileReader(fileAdditional));
		additionalsCache = new HashMap<Long,List<String>>();
		descQueue = new SortedList<Desc>(COMPARATOR);
	}
	public int getCacheSize() {
		return cacheSize;
	}
	public SortedList<Desc> getDescQueue() {
		return descQueue;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public DescReader getReaderBase() {
		return readerBase;
	}
	public void setReaderBase(DescReader readerBase) {
		this.readerBase = readerBase;
	}
	public BufferedReader getReaderAdditional() {
		return readerAdditional;
	}
	public void setReaderAdditional(BufferedReader readerAdditional) {
		this.readerAdditional = readerAdditional;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public File getFileIndex() {
		return fileIndex;
	}
	public void setFileIndex(File fileIndex) {
		this.fileIndex = fileIndex;
	}
	public File getFileData() {
		return fileData;
	}
	public void setFileData(File fileData) {
		this.fileData = fileData;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public Store getStore() {
		return store;
	}
	public void prepareReaders() throws IOException {
		while (
				readerBase.hasNext()
			&& start.after(readerBase.peek().getTime())
		) {
			readerBase.next();
		}
		if (readerBase.hasNext()
				&& start.before(readerBase.peek().getTime())
				&& end.after(readerBase.peek().getTime())) {
			logger.info("File will be analyzed:{},{}, startTime:{}",fileBase.getName(), fileAdditional.getName(), readerBase.peek().getTime());
		} else {
			readerBase.close();
			readerBase = null;
			logger.info("File discarted:{}",fileBase.getName());
		}
		if (readerBase!=null) {
			Long seq = readerBase.peek().getSequence();
			String line;
			while ((line = readerAdditional.readLine())!=null
				&& Long.valueOf(line.substring(10, 17)) < seq) {}
			if (line != null
					&& Long.valueOf(line.substring(10, 17)) >= seq) {
				logger.info("File will be analyzed:{},{}, startTime:{}",fileBase.getName(), fileAdditional.getName(), readerBase.peek().getTime());
			} else {
				readerAdditional.close();
				readerAdditional = null;
				logger.info("File discarted:{}",fileAdditional.getName());
			}			
		}
	}
	public void populateCache() throws NumberFormatException, IOException {
		if (readerAdditional==null) {
			return;
		}
		if (additionalsCache.size() < cacheSize/2) {
			String line;
			Long seq;
			while ((line = readerAdditional.readLine()) != null
					&& additionalsCache.size() < cacheSize) {
				seq = Long.valueOf(line.substring(10, 17));
				if (!additionalsCache.containsKey(seq)) {
					additionalsCache.put(seq, new ArrayList<String>());
				}
				additionalsCache.get(seq).add(line);
			}
		}
	}
	public Map<Long, List<String>> getAdditionalsCache() {
		return additionalsCache;
	}
	public void setAdditionalsCache(Map<Long, List<String>> additionals) {
		this.additionalsCache = additionals;
	}
}
