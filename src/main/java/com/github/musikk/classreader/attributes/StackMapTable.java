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
package com.github.musikk.classreader.attributes;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;
import com.github.musikk.classreader.ClassReaderImpl;
import com.google.common.collect.Iterators;

public class StackMapTable extends AttributeInfo implements Iterable<StackMapFrame> {

	private boolean entriesAreParsed;
	private final byte[] tableBytes;
	private final int numberOfEntries;

	private final List<StackMapFrame> entries;

	public StackMapTable(byte[] tableBytes, int numberOfEntries) {
		this.tableBytes = tableBytes;
		this.numberOfEntries = numberOfEntries;
		this.entries = new ArrayList<>(numberOfEntries);
	}

	public List<StackMapFrame> getEntries() {
		ensureParsedEntries();
		return Collections.unmodifiableList(entries);
	}

	private void ensureParsedEntries() {
		if (!entriesAreParsed) {
			ClassReader reader = new ClassReaderImpl(new ByteArrayInputStream(tableBytes));
			// constant pool is not actually required...
			for (int i = 0; i < numberOfEntries; i++) {
				entries.add(StackMapFrame.getStackMapFrame(reader));
			}
			entriesAreParsed = true;
		}
	}

	@Override
	public Iterator<StackMapFrame> iterator() {
		ensureParsedEntries();
		return Iterators.unmodifiableIterator(entries.iterator());
	}

	static StackMapTable getStackMapTable(ClassReaderContext ctxt, int attributeLength) {
		ClassReader reder = ctxt.getClassReader();

		int numberOfEntries = reder.readUnsignedShort();
		byte[] tableBytes = new byte[attributeLength - 2];
		reder.readBytesFully(tableBytes);

		return new StackMapTable(tableBytes, numberOfEntries);
	}

}
