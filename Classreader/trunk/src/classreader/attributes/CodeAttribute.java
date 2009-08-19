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
package classreader.attributes;

import classreader.ClassReader;
import classreader.constantpool.ConstantPool;
import classreader.util.StreamUtils;

public class CodeAttribute extends AttributeInfo {

	private final int maxStack;
	private final int maxLocals;
	private final Code code;
	private final ExceptionTable exceptionTable;
	private final Attributes attributes;

	private CodeAttribute(int maxStack, int maxLocals, Code code,
			ExceptionTable exceptionTable, Attributes attributes) {
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.code = code;
		this.exceptionTable = exceptionTable;
		this.attributes = attributes;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public Code getCode() {
		return code;
	}

	public ExceptionTable getExceptionTable() {
		return exceptionTable;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	protected static CodeAttribute getCode(int attributeLength, byte[] info,
			ConstantPool constantPool) {

		ClassReader classReader = StreamUtils.createClassReader(info,
				constantPool);

		int maxStack = classReader.readShort();
		int maxLocals = classReader.readShort();
		int codeLength = classReader.readInt();

		byte[] codeBytes = new byte[codeLength];
		classReader.readBytesFully(codeBytes);
		Code code = Code.getCode(codeBytes, constantPool);

		int exceptionTableLength = classReader.readShort();
		ExceptionTable exceptionTable = ExceptionTable.getExceptionTable(
				classReader, exceptionTableLength);

		int attributesCount = classReader.readShort();
		Attributes attributes = Attributes.getAttributes(classReader,
				attributesCount);

		return new CodeAttribute(maxStack, maxLocals, code, exceptionTable,
				attributes);
	}

}
