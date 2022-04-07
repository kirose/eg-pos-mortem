package com.fimet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Queue;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class FileUtils {
	private static final int SIZE_BUFFER = 512;
	private FileUtils() {
	}
	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] bytes = new byte[512];
		ByteBuilder bb = new ByteBuilder();
		int ln;
		while((ln = in.read(bytes)) > 0) {
			bb.append(bytes, 0, ln);
		}
		return bb.getBytes();
	}
	public static String readString(InputStream in) throws IOException {
		StringBuffer s = new StringBuffer();
		byte[] bytes = new byte[512];
		int ln;
		while((ln = in.read(bytes)) > 0) {
			s.append(new String(bytes, 0, ln));
		}
		return s.toString();
	}
	public static String readContents(File file) {
		if (file == null || !file.exists() || file.isDirectory())
			return null;
		InputStream in = null;
		StringBuilder content = new StringBuilder();
		try {
			in = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int i = 0;
			while ((i = in.read(bytes))>0) {
				content.append(new String(bytes,0,i));
			}
		} catch (IOException e) {
		} finally {
			close(in);
		}
		return content.toString();
	}
	public static byte[] readBytesContents(File file) {
		if (file == null || !file.exists() || file.isDirectory())
			return null;
		if (file.length() > 0) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				byte[] bytes = new byte[(int)file.length()];
				in.read(bytes);
				return bytes;
			} catch (IOException e) {
				return null;
			} finally {
				close(in);
			}
		} else {
			return new byte[0];
		}
	}
	public static void appendContents(File file, byte[] contents) {
		if (file == null)
			return;
		OutputStream out = null;
		try {
			out = new FileOutputStream(file,true);
			out.write(contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void writeContents(File file, byte[] contents) {
		if (file == null)
			return;
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void writeContents(File file, String contents) {
		if (file == null || file.isDirectory())
			return;
		java.io.OutputStreamWriter out = null;
		try {
			out = new java.io.FileWriter(file);
			out.write(contents == null ? "" : contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void appendContents(File file, String contents) {
		if (file == null || file.isDirectory())
			return;
		java.io.OutputStreamWriter out = null;
		try {
			out = new java.io.FileWriter(file, true);
			out.write(contents == null ? "" : contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void createSubdirectories(File file) {
		String path = file.getAbsolutePath().replace('\\', '/');
		path = path.substring(0,path.lastIndexOf('/'));
		new File(path).mkdirs();
	}
	public static void write(File input, File output, long start, int length) {
		byte[] bytes = new byte[512];
		RandomAccessFile reader = null;
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(output);
			reader = new RandomAccessFile(input,"r");
			reader.seek(start);
			int count = 0;
			int read;
			while (count < length && (read = reader.read(bytes, 0, Math.min(length-count, SIZE_BUFFER))) > 0) {
				writer.write(bytes,0,read);
				count += read;
			}
		} catch (IOException ex) {
			throw new RuntimeException("Cannot read file "+(input != null ? input.getName() : null), ex);
		} finally {
			close(reader);
			close(writer);
		}
	}
	public static void copy(File input, File output) {
		byte[] bytes = new byte[512];
		RandomAccessFile reader = null;
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(output);
			reader = new RandomAccessFile(input,"r");
			int read;
			while ((read = reader.read(bytes)) > 0) {
				writer.write(bytes,0,read);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Cannot copy file "+(input != null ? input.getName() : null), ex);
		} finally {
			close(reader);
			close(writer);
		}
	}
	public static void deleteFiles(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childs = file.listFiles();
			if (childs != null && childs.length > 0) {
				for (File f : childs) {
					deleteFiles(f);
				}
			}
		}
	}
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childs = file.listFiles();
			if (childs != null && childs.length > 0) {
				for (File f : childs) {
					delete(f);
				}
			}
			file.delete();
		}
	}
	public static String getExtension(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		int index = file.getName().lastIndexOf('.');
		if (index > 0) {
			return file.getName().substring(index+1);
		} else {
			return null;
		}
	}
	public static String getSimpleName(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		int index = file.getName().lastIndexOf('.');
		if (index > 0) {
			return file.getName().substring(0,index);
		} else {
			return file.getName();
		}
	}
	public static void write(File from, OutputStream to, boolean closeTo) {
		if (from != null && from.exists() && !from.isDirectory() && from.length() > 0) {
			try {
				write(new FileInputStream(from), to, true, closeTo);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public static void write(InputStream from, File to, boolean append, boolean closeFrom) {
		if (from != null && to != null) {
			try {
				write(from, new FileOutputStream(to,append), closeFrom, true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public static void write(InputStream from, OutputStream to, boolean closeFrom, boolean closeTo) {
		try {
			byte[] bytes = new byte[1024];
			int i = 0;
			while ((i = from.read(bytes))>0) {
				to.write(bytes,0,i);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (closeFrom) close(from);
			if (closeTo) close(to);
		}
	}
	public static long writeAndCountLines(InputStream from, File to, boolean closeFrom) {
		if (from != null && to != null) {
			try {
				return writeAndCountLines(from, new FileOutputStream(to), closeFrom, true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return -1L;
	}
	public static long writeAndCountLines(InputStream from, OutputStream to, boolean closeFrom, boolean closeTo) {
		try {
			byte[] bytes = new byte[1024];
			int i = 0;
			long numOfLines = 0L;
			while ((i = from.read(bytes))>0) {
				for (int j = 0; j < i; j++) {
					if (bytes[i] == (byte)10) {
						numOfLines++;
					}
				}
				to.write(bytes,0,i);
			}
			return numOfLines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (closeFrom) close(from);
			if (closeTo) close(to);
		}
	}
	public static void close(java.io.Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ex) {}
		}		
	}
	public static void rcopy(File from, File to) {
		rcopy(from, to, null);
	}
	public static void rcopy(File from, File to, String filter) {
		if (filter!=null) {
			filter = filter.toLowerCase();
		}
		if (from.isFile()) {
			if (filter==null||from.getName().toLowerCase().endsWith(filter)) {
				copy(from, to);
			}
		} else {
			Queue<File> queue = new java.util.LinkedList<File>();
			queue.add(from);
			to.mkdirs();
			while (!queue.isEmpty()) {
				File dirFrom = queue.poll();
				File[] files = dirFrom.listFiles();
				if(files!=null&&files.length>0) {
					makePathTo(from, to, dirFrom).mkdir();
					for (File fileFrom : files) {
						if (fileFrom.isDirectory()) {
							queue.add(fileFrom);
						} else if (filter == null || fileFrom.getName().toLowerCase().endsWith(filter)){
							File fileTo = makePathTo(from, to, fileFrom);
							copy(fileFrom, fileTo);
						}
					}
				}
			}
		}
	}
	public static void rdeleteFiles(File from) {
		if (from.isFile()) {
			from.delete();
		} else {
			Queue<File> queue = new java.util.LinkedList<File>();
			queue.add(from);
			while (!queue.isEmpty()) {
				File dirFrom = queue.poll();
				File[] files = dirFrom.listFiles();
				if(files!=null&&files.length>0) {
					for (File file : files) {
						if (file.isDirectory()) {
							queue.add(file);
						} else {
							file.delete();
						}
					}
				}
			}
		}
	}
	public static void rmove(File from, File to, String filter) {
		rcopy(from, to, filter);
		delete(from);
	}
	public static void rmove(File from, File to) {
		rmove(from, to, null);
	}
	private static File makePathTo(File dirFrom, File dirTo, File pathFrom) {
		if (pathFrom==dirFrom)
			return dirTo;
		String subpath = pathFrom.getAbsolutePath().substring(dirFrom.getAbsolutePath().length());
		return new File(dirTo,subpath);
	}
	public static String makeRelativeTo(File path, File to) {
		String absPath = path.getAbsolutePath();
		String absPathTo = to.getAbsolutePath();
		if (absPath.startsWith(absPathTo)) {
			return absPath.substring(absPathTo.length());
		} else {
			throw new IllegalArgumentException(absPath+" cannot be relative to "+absPathTo);
		}
	}
}
