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
package net.sourceforge.classreader.constantpool;

import net.sourceforge.classreader.ClassReader;

public abstract class ConstantPoolInfo {

	private int tag;

	protected void setTag(int tag) {
		this.tag = tag;
	}

	public int getTag() {
		return tag;
	}

	protected static ConstantPoolInfo createConstantClassInfo(
			ClassReader classStream) {
		int nameIndex = classStream.readShort();
		ConstantPoolInfo cpi = new ConstantClassInfo(nameIndex);
		return cpi;
	}

	protected static ConstantPoolInfo createConstantFieldrefInfo(
			ClassReader classStream) {
		int classIndex = classStream.readShort();
		int nameAndTypeIndex = classStream.readShort();
		ConstantPoolInfo cfr = new ConstantFieldrefInfo(classIndex,
				nameAndTypeIndex);
		return cfr;
	}

	protected static ConstantPoolInfo createConstantMethodrefInfo(
			ClassReader classStream) {
		int classIndex = classStream.readShort();
		int nameAndTypeIndex = classStream.readShort();
		ConstantPoolInfo cpi = new ConstantMethodrefInfo(classIndex,
				nameAndTypeIndex);
		return cpi;
	}

	protected static ConstantPoolInfo createInterfaceMethodrefInfo(
			ClassReader classStream) {
		int classIndex = classStream.readShort();
		int nameAndTypeIndex = classStream.readShort();
		ConstantPoolInfo cpi = new InterfaceMethodrefInfo(classIndex,
				nameAndTypeIndex);
		return cpi;
	}

	protected static ConstantPoolInfo createStringInfo(ClassReader classStream) {
		int stringIndex = classStream.readShort();
		ConstantPoolInfo cpi = new StringInfo(stringIndex);
		return cpi;
	}

	protected static ConstantPoolInfo createIntegerInfo(ClassReader classStream) {
		int value = classStream.readInt();
		ConstantPoolInfo cpi = new IntegerInfo(value);
		return cpi;
	}

	protected static ConstantPoolInfo createFloatInfo(ClassReader classStream) {
		float value = classStream.readFloat();
		ConstantPoolInfo cpi = new FloatInfo(value);
		return cpi;
	}

	protected static ConstantPoolInfo createLongInfo(ClassReader classStream) {
		long value = classStream.readLong();
		ConstantPoolInfo cpi = new LongInfo(value);
		return cpi;
	}

	protected static ConstantPoolInfo createDoubleInfo(ClassReader classStream) {
		double value = classStream.readDouble();
		ConstantPoolInfo cpi = new DoubleInfo(value);
		return cpi;
	}

	protected static ConstantPoolInfo createNameAndTypeInfo(
			ClassReader classStream) {
		int nameIndex = classStream.readShort();
		int descriptorIndex = classStream.readShort();
		ConstantPoolInfo cpi = new NameAndTypeInfo(nameIndex, descriptorIndex);
		return cpi;
	}

	protected static ConstantPoolInfo createUtf8Info(ClassReader classStream) {
		String utf8 = classStream.readUtf8String();
		ConstantPoolInfo cpi = new Utf8Info(utf8);
		return cpi;
	}

}
