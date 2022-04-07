package com.fimet.utils;


import com.fimet.utils.converter.IConverter;
/**
 * Useful class for read byte contents
 * @author Marco Antonio
 *
 */
public class ByteBuffer implements IReader {

	private byte[] bytes;
	private int cursor;
	private int length;
	
	public ByteBuffer(byte[] bytes){
		if (bytes == null) {
			throw new NullPointerException("bytes are null");
		}
		this.bytes = bytes;
		this.cursor = 0;
		this.length = bytes.length;
	}
	@Override
	public byte[] peek(int length) {
		if (cursor < this.length) {
			byte[] bytes = new byte[minLength(length)];
			System.arraycopy(this.bytes, cursor, bytes, 0, minLength(length));
			return bytes;
		} else {
			return new byte[0];
		}
	}
	@Override
	public byte[] read(int length) {
		if (cursor < this.length) {
			byte[] bytes = new byte[length = minLength(length)];
			System.arraycopy(this.bytes, cursor, bytes, 0, length);
			move(length);
			return bytes;
		} else {
			return new byte[0];
		}
	}
	public byte readByte() {
		return bytes[cursor++];
	}
	public char readChar() {
		return (char)bytes[cursor++];
	}
	public int readShort() {
		int num =
				 ((bytes[cursor] & 0xFF)<<8)
				|((bytes[cursor+1] & 0xFF));
		move(2);
		return num;
	}
	public int readInt() {
		int num =
				 ((bytes[cursor] & 0xFF)<<24)
				|((bytes[cursor+1] & 0xFF)<<16)
				|((bytes[cursor+2] & 0xFF)<<8)
				|(bytes[cursor+3] & 0xFF);
		move(4);
		return num;
	}
	public static void main(String[] args) {
		System.out.println(2L*2*2*2*2*2*2*2);//2#8 byte
		System.out.println(2L*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2);//2#16 short
		System.out.println(2L*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2);//2#32 int
		System.out.println(((byte)127)+" "+(((byte)127)&0xFF)+" "+((int)((byte)127)));
		System.out.println(((byte)128)+" "+(((byte)128)&0xFF)+" "+((int)((byte)128)));
		System.out.println(((byte)129)+" "+(((byte)129)&0xFF)+" "+((int)((byte)129)));
		System.out.println(((byte)130)+" "+(((byte)130)&0xFF)+" "+((int)((byte)130)));
		System.out.println(((byte)255)+" "+(((byte)255)&0xFF)+" "+((int)((byte)255)));
		System.out.println(((byte)256)+" "+(((byte)256)&0xFF)+" "+((int)((byte)256)));
		System.out.println((-1)+" "+(-1&0xFF));//byte
		System.out.println((-1)+" "+(-1&0xFFFF));//short
		System.out.println((-1)+" "+(-1&0xFFFFFFFFL));//int
		System.out.println((-1)+" "+(-1&0xFFFFFFFFFFFFFFFFL));//long
	}
	public String readString(int length) {
		byte[] bytes = new byte[length];
		System.arraycopy(this.bytes, cursor, bytes, 0, length);
		move(length);
		return new String(bytes);
	}
	@Override
	public void convert(IConverter converter) {
		if (cursor < this.length) {
			byte[] converted = converter.convert(ByteUtils.subArray(this.bytes, cursor));
			bytes = ByteUtils.concat(ByteUtils.subArray(bytes, 0, cursor), converted);
			length = bytes.length;
		}
	}
	private int minLength(int length) {
		return Math.min(length, this.length-cursor);
	}
	@Override
	public void assertChar(char c) {
		if (c != ((char) bytes[cursor])) {
			throw new RuntimeException("Expected char '"+c+"' found: '"+((char) bytes[cursor])+"'");
		}
		cursor++;
	}
	@Override
	public String toString() {
		return new String(bytes, cursor, bytes.length-cursor);
	}
	@Override
	public boolean hasNext() {
		return cursor < length;
	}
	@Override
	public int length() {
		return length;
	}
	@Override
	public int position() {
		return cursor;
	}
	@Override
	public void move(int no) {
		if (cursor + no > length || cursor + no < 0) {
			throw new IndexOutOfBoundsException();
		}
		cursor += no;
	}
	public boolean startsWith(String value) {
		return startsWith(value.getBytes());
	}
	public boolean startsWith(byte[] value) {
		return startsWith(value, cursor);
	}
	@Override
	public boolean startsWith(char c) {
		return bytes[cursor] == (byte)c;
	}
	public boolean startsWith(byte[] value, int offset) {
		int index = 0;
		int length = value.length;
		if (offset >= 0 && offset <= bytes.length - length) {
			do {
				--length;
				if (length < 0) {
					return true;
				}
			} while (bytes[offset++] == value[index++]);

			return false;
		} else {
			return false;
		}
	}
	public int indexOf(char b) {
		return indexOf((byte)b);
	}
	public int indexOf(byte b) {
		for (int i = cursor; i < bytes.length; i++) {
			if (bytes[i] == b)
				return i-cursor;
		}
		return -1;
	}
	public int indexOf(byte[] text) {
		return indexOf(bytes, 0, bytes.length, text, 0, text.length, cursor);
	}
	public int indexOf(String text) {
		return indexOf(bytes, 0, bytes.length, text.getBytes(), 0, text.length(), cursor);
	}
	public int indexOf(String text, int startFrom) {
		return indexOf(bytes, 0, bytes.length, text.getBytes(), 0, text.length(), startFrom);
	}
	static int indexOf(byte[] array, int startArray, int endArray, byte[] subarray, int startSubarray, int endSubarray, int startFrom) {
		if (startFrom >= endArray) {
			return endSubarray == 0 ? endArray : -1;
		} else {
			if (startFrom < 0) {
				startFrom = 0;
			}

			if (endSubarray == 0) {
				return startFrom;
			} else {
				byte var7 = subarray[startSubarray];
				int var8 = startArray + (endArray - endSubarray);

				for (int var9 = startArray + startFrom; var9 <= var8; ++var9) {
					if (array[var9] != var7) {
						do {
							++var9;
						} while (var9 <= var8 && array[var9] != var7);
					}

					if (var9 <= var8) {
						int startArray0 = var9 + 1;
						int startArray1 = startArray0 + endSubarray - 1;

						for (int startArray2 = startSubarray + 1; startArray0 < startArray1 && array[startArray0] == subarray[startArray2]; ++startArray2) {
							++startArray0;
						}

						if (startArray0 == startArray1) {
							return var9 - startArray;
						}
					}
				}

				return -1;
			}
		}
	}
}
