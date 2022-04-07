package com.fimet.eglobal.desc;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.eglobal.utils.FileReader;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DescReader implements Closeable {
	private static Logger logger = LoggerFactory.getLogger(DescReader.class);
	private FileReader reader;
	private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
	private byte[] lineBytes;
	private Desc next;
	private Date lastTime;
	private String yyMMdd;
	public DescReader(File file) throws IOException {
		String name = file.getName();
		yyMMdd = name.substring(8,10)+name.substring(4, 6)+name.substring(6,8);
		this.reader = new FileReader(file);
	}
	public void close() {
		try {
			reader.close();
		} catch (Exception e) {}
	}
	public boolean hasNext() {
		if (next!=null) {
			return true;
		}
		next = parse();
		if (next!=null) {
			lastTime = next.getTime();
		}
		return next != null;
	}
	public Desc next() {
		Desc n = next;
		next = null;
		return n;
	}
	public Desc peek() {
		return next;
	} 
	private Desc parse() {
		lineBytes = reader.nextLine();
		if (lineBytes == null) {
			return null;
		}
		String line = new String(lineBytes);
		Desc r = new Desc();
		r.setLine(line);
		Long seq = Long.valueOf(line.substring(4, 11));
		r.setSequence(seq);
		try {
			String datetime = line.substring(90, 96);
			r.setTime(format.parse(yyMMdd+datetime));
		} catch (ParseException e) {
			logger.error("Date parse error",e);
			throw new RuntimeException(e);
		}
		return r;
	}
	public String toString() {
		return next!=null?next.toString():null;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public static void main(String[] args) throws IOException {
		//File file = new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Desc\\BBVA-ACQ-INT-B1-01_220327.2");
		File file = new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Desc\\BBVA-ISS-VISA-MC-B1-01_220327.1");
		DescReader reader = new DescReader(file);
		
		while (reader.hasNext()) {
			Desc raw = reader.next();
			System.out.println(raw);
		}
		reader.close();
	}
}
