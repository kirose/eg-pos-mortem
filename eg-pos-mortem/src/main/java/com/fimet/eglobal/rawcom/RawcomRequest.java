package com.fimet.eglobal.rawcom;

import java.io.File;
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
import com.fimet.eglobal.store.Store;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;


public class RawcomRequest {
	private static Logger logger = LoggerFactory.getLogger(RawcomRequest.class);
	private static final Comparator<Rawcom> COMPARATOR = (l, r)-> {
		return l.getTime().compareTo(r.getTime());
	};
	private Date start;
	private Date end;
	private long index = 0;
	private File fileIndex;
	private File fileData;
	private List<File> files;
	private RawcomCache cache;
	private Store store;
	private Map<String, RawcomReader> readers;
	private int cacheSize;
	Map<String, Connection> mapConnections;
	private int requestTimeout;
	public RawcomRequest(Date start, Date end, List<File> files, Map<String, Connection> mapConnections, int cacheSize, int requestTimeout) {
		this.start = start;
		this.end = end;
		this.files = files;
		this.cacheSize = cacheSize;
		this.requestTimeout = requestTimeout;
		this.mapConnections = mapConnections;
		readers = new HashMap<String, RawcomReader>();
		cache = new RawcomCache();
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
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public Store getStore() {
		return store;
	}
	public RawcomCache getCache() {
		return cache;
	}
	public void setCache(RawcomCache cache) {
		this.cache = cache;
	}
	public Map<String, RawcomReader> getReaders() {
		return readers;
	}
	public void setReaders(Map<String, RawcomReader> readers) {
		this.readers = readers;
	}
	public void prepareReaders() throws IOException {
		RawcomReader reader = null;
		for (File file : files) {
			reader = new RawcomReader(file);
			while (
				reader.hasNext()
				&& start.after(reader.peek().getTime())
			) {
				reader.next();
			}
			if (reader.hasNext()
					&& start.before(reader.peek().getTime())
					&& end.after(reader.peek().getTime())) {
				logger.info("File will be analyzed:{}, startTime:{}",file.getName(), reader.peek().getTime());
				readers.put(file.getName(), reader);
			} else {
				reader.close();
				logger.info("File discarted:{}",file.getName());
			}
		}
	}
	public void populateCache() {
		cache.populate();
	}
	public class RawcomCache {
		private SortedList<Rawcom> sortedList;
		private Date time1;
		private Date time2;
		private List<String> toRemove;
		public RawcomCache() {
			time1 = start;
			time2 = new Date(Math.min(time1.getTime() + requestTimeout, end.getTime()));
			sortedList = new SortedList<Rawcom>(Rawcom.class, COMPARATOR);
			toRemove = new ArrayList<String>();
		}
		public SortedList<Rawcom> getSortedList() {
			return sortedList;
		}
		public void setSortedList(SortedList<Rawcom> sortedList) {
			this.sortedList = sortedList;
		}
		public void populate() {
			if (readers.isEmpty()) {
				logger.debug("No readers");
				return;
			}
			RawcomReader reader = null;
			int totalToRead = cacheSize - sortedList.size();
			int maxPerFile = totalToRead / readers.size();
			Rawcom r;
			while (totalToRead > 0 && !readers.isEmpty()) {
				calculateNextTimeRange();
				for (Map.Entry<String, RawcomReader> e : readers.entrySet()) {
					reader = e.getValue();
					for (int i = 0; i < maxPerFile && reader.hasNext(); i++) {
						r = reader.peek();
						if (time2.after(r.getTime())) {
							r = reader.next();
							Connection connection = mapConnections.get(r.getIap());
							IParser parser = connection.getParser();
							if (parser == null) {
								throw new RuntimeException("No parser configured for dispatcher "+r.getDispatcher());
							}
							byte[] iso = r.getIsoMessage();
							if (connection.isSanitizeHex()) {
								iso = sanitizeHex(iso);
							}
							IMessage msg = parser.parseMessage(iso);
							String mti = (String)msg.getProperty(IMessage.MTI);
							r.setMti(Integer.parseInt(mti));
							r.setType(RawcomType.get(mti));
							r.setMessage(msg);
							sortedList.add(r);
						} else {
							break;
						}
					}
				}
				validateReaders();
			}
		}
		private void validateReaders() {
			for (Map.Entry<String, RawcomReader> e : readers.entrySet()) {
				RawcomReader reader = e.getValue();
				if (!reader.hasNext() || reader.peek().getTime().after(end)) {
					toRemove.add(e.getKey());
				}
			}
			if (!toRemove.isEmpty()) {
				for (String key : toRemove) {
					readers.remove(key);
					logger.info("Reader removed {}, no more records to read.", key);
				}
				toRemove.clear();
			}			
		}
		private void calculateNextTimeRange() {
			try {
				long minLastTime = time2.getTime();
				RawcomReader reader;
				for (Map.Entry<String, RawcomReader> e : readers.entrySet()) {
					reader = e.getValue();
					Date lastTime = reader.getLastTime();
					if (time2.after(lastTime)) {
						minLastTime = Math.min(minLastTime, lastTime.getTime()); 
					}
					if (lastTime.after(end)) {
						reader.close();
					}
				}
				time1 = new Date(minLastTime);
				time2 = new Date(Math.min(time1.getTime() + requestTimeout+ 100, end.getTime())); 
			} catch (Exception e) {
				logger.error("error",e);
			}		
		}
	}

	private byte[] sanitizeHex(byte[] iso) {
		for (int i = 0; i < iso.length; i++) {
			if (iso[i] == (byte)42) {
				iso[i++] = (byte)50;//2
				iso[i] = (byte)65;//A
			}
		}
		return iso;
	}
}
