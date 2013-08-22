/*
 * Copyright (c) 2009, Werner Hahn
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

import com.github.musikk.classreader.constantpool.ConstantPool;

/**
 * A <code>ClassReader</code> provides methods for reading from a stream that
 * are relevant in the context of parsing a binary file, especially a class file
 * in this context.
 * 
 * @author Werner Hahn
 * 
 */
public interface ClassReader {

	/**
	 * Reads a signed {@code long}.
	 * 
	 * @return the {@code long} value
	 */
	long readLong();

	/**
	 * Reads a signed {@code byte}.
	 * 
	 * @return the {@code byte} value
	 */
	byte readByte();

	/**
	 * Reads an unsigned {@code byte}. Because there is no unsigned {@code byte}
	 * data type in Java it is simulated by a {@code short} value.
	 * 
	 * @return the unsigned {@code byte} value as a {@code short}
	 */
	short readUnsignedByte();

	/**
	 * Reads a signed {@code int}.
	 * 
	 * @return the {@code int} value
	 */
	int readInt();

	/**
	 * Reads an unsigned {@code int}. Because there is no unsigned {@code int}
	 * data type in Java it is simulated by a {@code long} value.
	 * 
	 * @return the unsigned {@code int} value as a {@code long}
	 */
	long readUnsignedInt();

	/**
	 * Reads a signed {@code short}.
	 * 
	 * @return the {@code short} value
	 */
	short readShort();

	/**
	 * Reads an unsigned {@code short}. Because there is no unsigned {@code
	 * short} data type in Java it is simulated by an {@code int} value.
	 * 
	 * @return the unsigned {@code short} value as an {@code int}
	 */
	int readUnsignedShort();

	/**
	 * Reads a {@code float}.
	 * 
	 * @return the {@code float} value
	 */
	float readFloat();

	/**
	 * Reads a {@code double}.
	 * 
	 * @return the {@code double} value
	 */
	double readDouble();

	/**
	 * Reads bytes from the underlying stream until the provided buffer is full.
	 * This method behaves like {@code readBytesFully(buffer, 0, buffer.length}.
	 * 
	 * @param buffer
	 *            the buffer to fill
	 */
	void readBytesFully(byte[] buffer);

	/**
	 * Reads bytes from the underlying stream until the provided buffer is full.
	 * {@code length} bytes are written into the buffer starting from position
	 * {@code offset}.
	 * 
	 * @param buffer
	 *            the buffer to fill
	 * @param offset
	 *            the position in the buffer to begin with
	 * @param length
	 *            the number of bytes to read
	 */
	void readBytesFully(byte[] buffer, int offset, int length);

	/**
	 * Reads an UTF-8 {@code String}.
	 * 
	 * @return the UTF-8 {@code String} value
	 */
	String readUtf8String();

	/**
	 * Saves the current {@link ConstantPool} to provide an easy way to pass it
	 * around.
	 * 
	 * @param constantPool
	 *            the {@code ConstantPool} to save
	 */
	void setConstantPool(ConstantPool constantPool);

	/**
	 * Retrieves the previously saved {@link ConstantPool}.
	 * 
	 * @return the {@code ConstantPool} or {@code null} if no {@code
	 *         ConstantPool} has yet been saved.
	 */
	ConstantPool getConstantPool();

	/**
	 * The position in the stream this {@code ClassReader} is currently at.
	 * 
	 * @return the current position
	 */
	int getPosition();

}
