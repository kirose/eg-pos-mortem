package com.fimet.eglobal.store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataReader {
	private static Logger logger = LoggerFactory.getLogger(DataReader.class);
	private RandomAccessFile reader;
	public DataReader(File file) throws IOException {
		reader = new RandomAccessFile(file,"r");
	}
	public String read(Index index) throws IOException {
		reader.seek(index.offset);
		byte[] data = new byte[index.length];
		int ln = reader.read(data);
		if (ln != index.length) {
			logger.warn("Expected length:{}, actual:{}, key{}",index.length, ln, index.key);
		}
		return new String(data);
	}
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			logger.error("Error on close file",e);
		}
	}
	public static void main(String[] args) throws IOException {
		IndexReader ir = new IndexReader(new File("Analyzed/Rawcom-index-20220401-235735.txt"));
		DataReader dr = new DataReader(new File("Analyzed/Rawcom-data-20220401-235735.txt"));
		Index n;
		while ((n = ir.next())!=null) {
			String data = dr.read(n);
			System.out.print(n+":"+data);
		}
		dr.close();
		ir.close();
	}
}
