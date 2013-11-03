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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReaderImpl implements ClassReader {

	private final InputStream is;
	private int position;

	public ClassReaderImpl(InputStream is) {
		this.is = is;
		this.position = 0;
	}

	@Override
	public byte readByte() {
		try {
			int nextByte = is.read();
			if (nextByte == -1) {
				throw new RuntimeException("end of file");
			}
			position++;
			return (byte) nextByte;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void readBytesFully(byte[] buffer) {
		readBytesFully(buffer, 0, buffer.length);
	}

	@Override
	public void readBytesFully(byte[] buffer, int offset, int length) {

		int bytesLeft = length;

		while (bytesLeft > 0) {
			try {
				int bytesRead = is.read(buffer, length - bytesLeft, bytesLeft);
				bytesLeft -= bytesRead;
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		position += length;

	}

	@Override
	public int readInt() {
		byte[] buffer = new byte[4];
		readBytesFully(buffer);
		int result = 0;
		for (byte b : buffer) {
			result <<= 8;
			result |= b & 0xFF;
		}
		return result;
	}

	@Override
	public long readUnsignedInt() {
		return readInt() & 0xFFFFFFFFL;
	}

	@Override
	public long readLong() {
		byte[] buffer = new byte[8];
		readBytesFully(buffer);
		long result = 0;
		for (byte b : buffer) {
			result <<= 8;
			result |= b & 0xFF;
		}
		return result;
	}

	@Override
	public short readShort() {
		short b1 = readUnsignedByte();
		short b2 = readUnsignedByte();
		return (short) ((b1 << 8) + b2);
	}

	@Override
	public int readUnsignedShort() {
		short b1 = readUnsignedByte();
		short b2 = readUnsignedByte();
		return ((b1 << 8) + (b2 << 0));
	}

	@Override
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public String readUtf8String() {
		try {
			String string = DataInputStream.readUTF(new DataInputStream(is));
			position += string.getBytes().length;
			return string;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short readUnsignedByte() {
		return (short) (readByte() & 0xFF);
	}

	@Override
	public int getPosition() {
		return position;
	}

}
