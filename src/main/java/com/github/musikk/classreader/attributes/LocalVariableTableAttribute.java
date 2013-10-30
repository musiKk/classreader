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
import java.util.List;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;

public class LocalVariableTableAttribute extends AttributeInfo {

	private final List<LocalVariableTableEntry> localVariableTableEntries;

	private LocalVariableTableAttribute(List<LocalVariableTableEntry> localVariableTableEntries) {
		this.localVariableTableEntries = localVariableTableEntries;
	}

	public List<LocalVariableTableEntry> getLocalVariableTableEntries() {
		return Collections.unmodifiableList(localVariableTableEntries);
	}

	protected static LocalVariableTableAttribute getLocalVariableTableAttribute(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int localVariableTableLength = reader.readUnsignedShort();
		List<LocalVariableTableEntry> localVariableTableEntries = new ArrayList<>(localVariableTableLength);
		for (int i = 0; i < localVariableTableLength; i++) {
			localVariableTableEntries.add(LocalVariableTableEntry.getLocalVariableTableEntry(ctxt));
		}

		return new LocalVariableTableAttribute(localVariableTableEntries);

	}

}
