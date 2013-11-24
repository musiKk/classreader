/*
 * Copyright (c) 2013, Werner Hahn
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ONANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.musikk.classreader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.CountingInputStream;

public class ClassReaderImpl implements ClassReader {

	private CountingInputStream countingInputStream;
	private DataInput dataInput;

	public ClassReaderImpl(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			int read = -1;
			while ((read = is.read(buf)) != -1) {
				baos.write(buf, 0, read);
			}
			countingInputStream = new CountingInputStream(new ByteArrayInputStream(baos.toByteArray()));
			dataInput = new DataInputStream(countingInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte readByte() {
		try {
			return dataInput.readByte();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void readBytesFully(byte[] buffer) {
		try {
			dataInput.readFully(buffer, 0, buffer.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void readBytesFully(byte[] buffer, int offset, int length) {
		try {
			dataInput.readFully(buffer, offset, length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int readInt() {
		try {
			return dataInput.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long readUnsignedInt() {
		try {
			return dataInput.readInt() & 0xFFFFFFFFL;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long readLong() {
		try {
			return dataInput.readLong();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short readShort() {
		try {
			return dataInput.readShort();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int readUnsignedShort() {
		try {
			int i1 = countingInputStream.read();
			int i2 = countingInputStream.read();
			if (i2 == -1) {
				throw new RuntimeException("EOF");
			}
			return (i1 << 8) | i2;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double readDouble() {
		try {
			return dataInput.readDouble();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public float readFloat() {
		try {
			return dataInput.readFloat();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readUtf8String() {
		try {
			return dataInput.readUTF();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short readUnsignedByte() {
		try {
			return (short) dataInput.readUnsignedByte();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getPosition() {
		return countingInputStream.getCount();
	}

	@Override
	public void close() {
		/*
		 * Since the streams are only filters ultimately working on a byte array
		 * on the heap, no actual close operation has to be invoked. The
		 * references are set to null so the byte array may be garbage
		 * collected.
		 */
		countingInputStream = null;
		dataInput = null;
	}

}
