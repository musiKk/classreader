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
package net.sourceforge.classreader.attributes;

import net.sourceforge.classreader.ClassReader;

public class LocalVariableTableEntry {

	private final int length;
	private final int nameIndex;
	private final int descriptorIndex;
	private final int index;

	private LocalVariableTableEntry(int length, int nameIndex,
			int descriptorIndex, int index) {
		this.length = length;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.index = index;
	}

	public int getLength() {
		return length;
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	public int getIndex() {
		return index;
	}

	protected static LocalVariableTableEntry getLocalVariableTableEntry(
			ClassReader classReader) {

		int length = classReader.readShort();
		int nameIndex = classReader.readShort();
		int descriptorIndex = classReader.readShort();
		int index = classReader.readShort();

		return new LocalVariableTableEntry(length, nameIndex, descriptorIndex,
				index);

	}

}
