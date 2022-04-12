package com.fimet.eglobal.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fimet.utils.FileUtils;


public class FileReader implements Closeable {
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	private static final byte ASCII_NEW_LINE = (byte)10;
	private FileInputStream reader;
	private int sizeBuffer;
	private boolean eof;
	private byte[] buffer;
	private int indexEndOfLine = -1;
	public FileReader(File file) throws FileNotFoundException {
		reader = new FileInputStream(file);
		buffer = new byte[DEFAULT_BUFFER_SIZE];
		
	}
	public byte[] nextLine() {
		readLine();
		if (indexEndOfLine != -1) {
			byte[] array = new byte[indexEndOfLine+1];
			System.arraycopy(buffer, 0, array, 0, array.length);
			removeBufferedLine();
			return array;
		}
		return null;
	}
	private void removeBufferedLine() {
		if (indexEndOfLine != -1) {
			if (indexEndOfLine < buffer.length-1) {
				sizeBuffer -= indexEndOfLine + 1;
				for (int i = 0; i < sizeBuffer; i++) {
					buffer[i] = buffer[i+indexEndOfLine+1];
				}
			}
			indexEndOfLine = -1;
		}
	}
	private boolean readLine() {
		if (eof) {
			return false;
		}
		int index = -1;
		int offset = 0;
		while (!eof && (index = indexNewLine(offset))==-1) {
			offset = sizeBuffer;
			if (sizeBuffer == buffer.length) {
				byte[] newBuffer = new byte[buffer.length*2];
				System.arraycopy(buffer, 0, newBuffer, 0, sizeBuffer);
				buffer = newBuffer;
			}
			int length;
			try {
				length = reader.read(buffer, sizeBuffer, buffer.length-sizeBuffer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (length == -1) {
				eof = true;
			} else {
				sizeBuffer += length;
			}
		}
		if (index != -1) {
			indexEndOfLine = index;
			return true;
		}
		if (eof && sizeBuffer > 0) {
			indexEndOfLine = sizeBuffer;
			return true;
		}
		return false;
	}
	private int indexNewLine(int offset) {
		for (int i = offset; i < sizeBuffer; i++) {
			if (buffer[i] == ASCII_NEW_LINE) {
				return i;
			}
		}
		return -1;
	}
	public void close() {
		FileUtils.close(reader);
		eof = true;
	}
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Rawcom\\BBVA-ISS-VISA-MC-B1-01_220327.1");//new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Rawcom\\BBVA-ACQ-INT-B1-01_220327.1");
		FileReader reader = new FileReader(file);
		byte[] line;
		while ((line = reader.nextLine())!=null) {
			System.out.println(new String(line));
		}
		reader.close();
	}
}
