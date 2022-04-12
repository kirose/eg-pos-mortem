package com.fimet.eglobal.rawcom;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.eglobal.utils.FileReader;
import com.fimet.utils.ByteBuilder;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.FileUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RawcomReader implements Closeable {
	private static Logger logger = LoggerFactory.getLogger(RawcomReader.class);
	private static final Pattern HEADER_PATTERN = Pattern.compile("\\[T:\\s+([0-9\\:\\.]+)\\]\\[D:\\s+([0-9]+)\\]\\[C:\\s+([0-9]+)\\]\\[Iap:\\s+([^\\]]+)\\]\\[Lp:\\s+([^\\]]+)\\]\\[Rw:\\s+([^\\]]+)\\]\\[L:\\s+([0-9]+)\\]+");
	private FileReader reader;
	private String dateyymmdd;
	private SimpleDateFormat format = new SimpleDateFormat("yyMMdd HH:mm:ss.SSS");
	private byte[] lineBytes;
	private Rawcom next;
	private Date lastTime;
	public RawcomReader(File file) throws IOException {
		this.reader = new FileReader(file);
		int i = file.getName().lastIndexOf('.');
		dateyymmdd = file.getName().substring(i-6,i);
	}
	public void close() {
		FileUtils.close(reader);
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
	public Rawcom next() {
		Rawcom n = next;
		next = null;
		return n;
	}
	public Rawcom peek() {
		return next;
	} 
	private Rawcom parse() {
		lineBytes = reader.nextLine();
		if (lineBytes == null) {
			return null;
		}
		//[T:
		if (!(lineBytes[0] == (byte)'[' && lineBytes[1]==(byte)'T' && lineBytes[2]==(byte)':')) {
			return null;
		}
		String line = new String(lineBytes);
		Matcher m = HEADER_PATTERN.matcher(line);
		if (!m.find()) {
			return null;
		}
		Rawcom r = new Rawcom();
		String time = m.group(1);
		try {
			r.setTime(format.parse(dateyymmdd+" "+time));
		} catch (ParseException e) {
			logger.error("Date parse error",e);
			throw new RuntimeException(e);
		}
		r.setDispatcher(Integer.parseInt(m.group(2)));
		r.setPan(m.group(3));
		r.setIap(m.group(4));
		r.setListProcessor(m.group(5));
		r.setDirection(m.group(6).charAt(0));
		r.setLength(Integer.parseInt(m.group(7)));
		int i = line.indexOf("]", line.indexOf(m.group(7)+"]"));
		if (r.getLength() == lineBytes.length - i - 2) {
			r.setIsoMessage(ByteUtils.subArray(lineBytes, i+1, lineBytes.length-1));
		} else {
			int ln = r.getLength();
			int remaining = ln - (lineBytes.length - i - 2);
			ByteBuilder bb = new ByteBuilder(ByteUtils.subArray(lineBytes, i+1));
			byte[] b;
			while (remaining > 0 && (b = reader.nextLine())!=null) {
				remaining -= b.length;
				bb.append(b);
			}
			r.setIsoMessage(ByteUtils.subArray(bb.getBytes(),0,bb.length()-1));
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
		//File file = new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Rawcom\\BBVA-ACQ-INT-B1-01_220327.2");
		File file = new File("D:\\eclipse\\wsfimetboot\\eg-pos-mortem\\Rawcom\\BBVA-ISS-VISA-MC-B1-01_220327.1");
		RawcomReader reader = new RawcomReader(file);
		
		while (reader.hasNext()) {
			Rawcom raw = reader.next();
			System.out.println(raw);
		}
		reader.close();
	}
}
