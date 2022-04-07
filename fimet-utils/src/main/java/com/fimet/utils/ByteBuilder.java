package com.fimet.utils;

public class ByteBuilder implements IWriter {

	private static final int DEFAULT_SIZE = 16; 
	private byte[] bytes;
	private int count;
	public ByteBuilder() {
		this(DEFAULT_SIZE);
	}
	public ByteBuilder(byte[] bytes) {
		this(bytes.length+16);
		append(bytes);
	}
	public ByteBuilder(int initialCapacity) {
		assert(initialCapacity > 0);
		bytes = new byte[initialCapacity];
	}
	public void move(int offset) {
		count += offset;
	}
	public int length() {
		return count;
	}
	public void append(String text) {
		append(text.getBytes());
	}
	public void append(byte[] bytes, int start, int end) {
		if (bytes == null) {
			throw new NullPointerException();
		}
		int length = end-start;
		if (length < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (end > bytes.length) {
			end = bytes.length;
		}
		if (length > this.bytes.length-count) {
			expand(count+end-start);
		}
		if (length > 0) {
			System.arraycopy(bytes, start, this.bytes, count, length);
			count += length;
		}
	}
	public void append(byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException();
		}
		if (bytes.length > this.bytes.length-count) {
			expand(count+bytes.length);
		}
		System.arraycopy(bytes, 0, this.bytes, count, bytes.length);
		count += bytes.length;
	}
	public void append(byte b) {
		if (1 > this.bytes.length-count) {
			expand(count+1);
		}
		this.bytes[count++] = b;
	}
	public void append(char c) {
		this.append((byte)c);
	}
	public void preappend(String text) {
		preappend(text.getBytes());
	}
	public void preappend(byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException();
		}
		if (bytes.length > this.bytes.length-count) {
			expand(count+bytes.length);
		}
		moveBytes(0,bytes.length);
		System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
	}
	public void insert(int count, String text) {
		insert(count, text.getBytes());
	}
	public void insert(int count, byte[] bytes) {
		if (count == 0) {
			preappend(bytes);
			return;
		}
		if (count == this.count) {
			append(bytes);
			return;
		}
		if (bytes == null) {
			throw new NullPointerException();
		}
		if (count < 0 || count > this.count) {
			throw new IndexOutOfBoundsException();
		}
		if (bytes.length > this.bytes.length-count) {
			expand(count+bytes.length);
		}
		moveBytes(count,bytes.length);
		System.arraycopy(bytes, 0, this.bytes, count, bytes.length);
	}
	public void replace(int count, String text) {
		replace(count, text.getBytes());
	}
	public void replace(int count, byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException();
		}
		if (count < 0 || count > this.count) {
			throw new IndexOutOfBoundsException();
		}
		if (bytes.length > this.bytes.length-count) {
			expand(count+bytes.length);
		}
		System.arraycopy(bytes, 0, this.bytes, count, bytes.length);
	}
	private void moveBytes(int start, int offset) {
		int ln = start - count;
		count += offset;
		for (int i = count - 1; i < start; i--) {
			bytes[i] = bytes[i-ln];
		}
	}
	public void delete(int start, int end) {
		if (start < 0) {
			throw new StringIndexOutOfBoundsException(start);
		} else {
			if (end >= this.count) {
				end = this.count;
				if (start == 0) {
					count = 0;
					return;
				}
			} 
			if (start > end) {
				throw new StringIndexOutOfBoundsException();
			} else {
				int length = end - start;
				if (length > 0) {
					System.arraycopy(bytes, start + length, bytes, start, count - end);
					this.count -= length;
				}
			}
		}
	}
	private void expand(int expected) {
		int newLength = bytes.length*2 + 2;
		if (newLength - expected < 0) {
			newLength = expected;
		}
		if (newLength < 0) {
			if (expected < 0) {
				throw new OutOfMemoryError();
			}
			newLength = Integer.MAX_VALUE;
		}
		byte[] newBytes = new byte[newLength];
		System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
		this.bytes = newBytes;
	}
	public byte[] getBytes() {
		byte[] copy = new byte[count];
		if (count > 0)
			System.arraycopy(bytes, 0, copy, 0, copy.length);
		return copy;
	}
	public byte[] empty() {
		byte[] bytes = getBytes();
		delete(0, count);
		return bytes;
	}
	public void setLength(int length) {
        if (length < 0)
            throw new ArrayIndexOutOfBoundsException(length);
        if (count < length) {
        	expand(length-count);
        }
        count = length;
	}
	public byte byteAt(int i) {
		if (i < 0 || i >= count) {
			throw new ArrayIndexOutOfBoundsException(i);
		}
		return bytes[i];
	}
	public String toString() {
		return new String(bytes, 0, count);
	}
	public byte[] subarray(int start) {
		return subarray(start, count);
	}
    /**
     * Returns a byte array that is a subarray of this ByteBuilder. The
     * subarray begins at the specified {@code start} and
     * extends to the character at index {@code end - 1}.
     * Thus the length of the substring is {@code end-start}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * new ByteBiulder(new byte[]{0,1,2,3,4,5,6,7,8}).subarray(4, 8) returns {4,5,6,7}
     * new ByteBiulder(new byte[]{0,1,2,3,4,5,6,7,8}).subarray(1, 5) returns {1,2,3,4}
     * </pre></blockquote>
     *
     * @param      beginIndex   the beginning index, inclusive.
     * @param      endIndex     the ending index, exclusive.
     * @return     the specified subarray.
     * @exception  IndexOutOfBoundsException  if the
     *             {@code start} is negative, or
     *             {@code end} is larger than the length of
     *             this {@code ByteBuilder} object, or
     *             {@code start} is larger than
     *             {@code end}.
     */
	public byte[] subarray(int start, int end) {
        if (start < 0) {
            throw new ArrayIndexOutOfBoundsException(start);
        }
        if (end > count) {
            throw new ArrayIndexOutOfBoundsException(end);
        }
        int length = end - start;
        if (length < 0) {
            throw new ArrayIndexOutOfBoundsException(length);
        }
    	byte[] subarray = new byte[length];
    	System.arraycopy(bytes, start, subarray, 0, length);
    	return subarray;
	}
}
