package com.fimet.eglobal.store;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.fimet.utils.ByteUtils;

public class IndexReader implements Closeable {
	private RandomAccessFile reader;
	byte[] buffer = new byte[512];
	private long currentPosition = - Index.INDEX_SIZE;
	private int index;
	private int size;
	private boolean eof;
	private Index next;
	public IndexReader(File file) throws IOException {
		reader = new RandomAccessFile(file, "r");
		checkBuffer();
		parseNext();
	}
	public boolean hasNext() throws IOException {
		if (next == null) {
			next = parseNext();
		}
		return next!=null;
	}
	public Index next() throws IOException {
		if (next!=null) {
			Index n = next;
			next = null;
			return n;
		} else {
			return parseNext(); 
		}
	}
	private Index parseNext() throws IOException {
		checkBuffer();
		if (size-index>Index.INDEX_SIZE) {
			Index next = new Index();
			currentPosition += Index.INDEX_SIZE;
			next.key = ByteUtils.toLong(buffer, index);
			index += 8;
			next.offset = ByteUtils.toLong(buffer, index);
			index += 8;
			next.length = ByteUtils.toInt(buffer, index);
			index += 4;
			return next;
		}
		return null;
	}
	private void checkBuffer() throws IOException {
		if (eof || size-index > Index.INDEX_SIZE) {
			return;
		}
		int ln;
		if (size == index) {
			size = 0;
			ln = reader.read(buffer);
		} else {
			size = size-index;
			for (int i = 0; i < size; i++) {
				buffer[i] = buffer[index+i];
			}
			ln = reader.read(buffer, size, buffer.length-size);
		}
		index = 0;
		if (ln==-1) {
			eof = true;
		} else {
			size += ln;
		}
	}
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
		}
	}
	public long getCurrentPosition() {
		return currentPosition;
	}
	public void seek(long position) throws IOException {
		currentPosition = position;
		resetBuffer();
		reader.seek(position);
	}
	private void resetBuffer() {
		next = null;
		size = 0;
		index = 0;
		eof = false;
	}
	public static void main(String[] args) throws IOException {
		IndexReader reader = new IndexReader(new File("Analyzed/Rawcom-index-20220409-194048.txt"));
		Index n;
		while ((n = reader.next())!=null) {
			System.out.println(n);
		}
		reader.close();
	}
}
