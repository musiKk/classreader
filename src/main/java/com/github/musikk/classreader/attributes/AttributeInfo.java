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

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;
import com.github.musikk.classreader.constantpool.ConstantPool;

public class AttributeInfo {

	private int attributeNameIndex;
	private int attributeLength;
	private byte[] info;

	private void setAttributeNameIndex(int attributeNameIndex) {
		this.attributeNameIndex = attributeNameIndex;
	}

	private void setAttributeLength(int attributeLength) {
		this.attributeLength = attributeLength;
	}

	private void setInfo(byte[] info) {
		this.info = info;
	}

	public int getAttributeNameIndex() {
		return attributeNameIndex;
	}

	public int getAttributeLength() {
		return attributeLength;
	}

	public byte[] getInfo() {
		return info;
	}

	protected static AttributeInfo getAttributeInfo(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int attributeNameIndex = reader.readUnsignedShort();
		int attributeLength = reader.readInt();

		ConstantPool constantPool = ctxt.getConstantPool();
		String attributeName = constantPool.getUtf8Info(attributeNameIndex).getValue();
		AttributeType attributeType = AttributeType.byName(attributeName);

		AttributeInfo attributeInfo;
		if (attributeType != null) {
			attributeInfo = attributeType.create(ctxt);
			// TODO remove once all attribute types are implemented
			if (attributeInfo == null) {
				reader.readBytesFully(new byte[attributeLength]); // just skip it
				attributeInfo = new AttributeInfo();
			}
		} else {
			attributeInfo = new AttributeInfo();
			byte[] bytes = new byte[attributeLength];
			reader.readBytesFully(bytes);
			attributeInfo.setInfo(bytes);
		}

		attributeInfo.setAttributeNameIndex(attributeNameIndex);
		attributeInfo.setAttributeLength(attributeLength);

		return attributeInfo;
	}

}
