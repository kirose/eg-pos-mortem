package com.fimet.eglobal.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.utils.ByteUtils;


public class Store {
	private static Logger logger = LoggerFactory.getLogger(Store.class);
	private FileOutputStream outputIndex;
	private FileWriter outputData;
	private File fileIndex;
	private File fileData;
	private long index;
	public Store(File index, File data) throws StoreException {
		try {
			this.outputIndex = new FileOutputStream(index);
			this.outputData = new FileWriter(data);
		} catch (IOException e) {
			throw new StoreException("Unable to instantiate Store",e);
		}
	}
	public void store(long key, String data) throws StoreException {
		int length = data.length() + 1;
		byte[] bytes = new byte[Index.INDEX_SIZE];
		ByteUtils.toBytes(key, bytes, 0);
		ByteUtils.toBytes(index, bytes, 8);
		ByteUtils.toBytes(length, bytes, 16);
		logger.info("Index:{},{},{}", key, index, length);
		try {
			outputData.write(data);
			outputData.write((byte)10);//(int)'\n');
		} catch (IOException e) {
			logger.error("Unable to write to {}, key:{}",fileData, key);
			throw new StoreException("Unable to store key:"+key,e);
		}
		try {
			outputIndex.write(bytes);// 8 bytes + 8 bytes + 4 bytes
		} catch (IOException e) {
			logger.error("Unable to write to {}, key:{}",fileIndex, key);
			throw new StoreException("Unable to store key:"+key,e);
		}
		this.index += length;
	}
	public void close() {
		try {
			outputIndex.close();
			outputData.close();
		} catch (IOException e) {
			logger.error("Error closing files:{},{}",fileIndex, fileData, e);
		}
	}
}
