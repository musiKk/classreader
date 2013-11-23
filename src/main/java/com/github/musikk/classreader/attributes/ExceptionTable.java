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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;
import com.google.common.collect.Iterators;

public class ExceptionTable implements Iterable<ExceptionTableEntry> {

	private final List<ExceptionTableEntry> exceptionTableEntries;

	private ExceptionTable(List<ExceptionTableEntry> exceptionTableEntries) {
		this.exceptionTableEntries = exceptionTableEntries;
	}

	public List<ExceptionTableEntry> getExceptionTableEntries() {
		return Collections.unmodifiableList(exceptionTableEntries);
	}

	@Override
	public Iterator<ExceptionTableEntry> iterator() {
		return Iterators.unmodifiableIterator(exceptionTableEntries.iterator());
	}

	protected static ExceptionTable getExceptionTable(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int exceptionTableLength = reader.readUnsignedShort();
		List<ExceptionTableEntry> exceptionTableEntries = new ArrayList<>(exceptionTableLength);
		for (int i = 0; i < exceptionTableLength; i++) {
			exceptionTableEntries.add(ExceptionTableEntry.getExceptionTableEntry(ctxt));
		}
		return new ExceptionTable(exceptionTableEntries);

	}

}
