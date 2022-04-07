package com.fimet.eglobal.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.eglobal.model.Connection;
import com.fimet.eglobal.store.Store;
import com.fimet.utils.FileUtils;


public class DescRequest {
	private static Logger logger = LoggerFactory.getLogger(DescRequest.class);
	private Date start;
	private Date end;
	private long index = 0;
	private File fileIndex;
	private File fileData;
	private File fileBase;
	private File fileAdditional;
	private Map<Long, List<String>> additionals;
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
		additionals = new HashMap<Long,List<String>>();
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
		if (additionals.size() < cacheSize/2) {
			String line;
			Long seq;
			while ((line = readerAdditional.readLine()) != null
					&& additionals.size() < cacheSize) {
				seq = Long.valueOf(line.substring(10, 17));
				if (!additionals.containsKey(seq)) {
					additionals.put(seq, new ArrayList<String>());
				}
				additionals.get(seq).add(line);
			}
		}
	}
	public Map<Long, List<String>> getAdditionals() {
		return additionals;
	}
	public void setAdditionals(Map<Long, List<String>> additionals) {
		this.additionals = additionals;
	}
	public void closeReaders() {
		FileUtils.close(readerBase);
		FileUtils.close(readerAdditional);
	}
}
