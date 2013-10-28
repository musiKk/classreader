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
package com.github.musikk.classreader.constantpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.musikk.classreader.ClassReader;

public class ConstantPool implements Iterable<ConstantPoolInfo> {

	public final static byte CONSTANT_Class = 7;
	public final static byte CONSTANT_Fieldref = 9;
	public final static byte CONSTANT_Methodref = 10;
	public final static byte CONSTANT_InterfaceMethodref = 11;
	public final static byte CONSTANT_String = 8;
	public final static byte CONSTANT_Integer = 3;
	public final static byte CONSTANT_Float = 4;
	public final static byte CONSTANT_Long = 5;
	public final static byte CONSTANT_Double = 6;
	public final static byte CONSTANT_NameAndType = 12;
	public final static byte CONSTANT_Utf8 = 1;
	public final static byte CONSTANT_MethodHandle = 15;
	public final static byte CONSTANT_MethodType = 16;
	public final static byte CONSTANT_InvokeDynamic = 18;

	private final List<ConstantPoolInfo> constantPoolInfos;

	private ConstantPool(List<ConstantPoolInfo> constantPoolInfos) {
		this.constantPoolInfos = constantPoolInfos;
	}

	public ConstantPoolInfo getConstantPoolInfo(int index) {
		ConstantPoolInfo cpi = constantPoolInfos.get(index - 1);
		if (cpi == null) {
			throw new IllegalArgumentException("The index " + index + " is invalid. "
					+ "Is the previous entry a long or double constant?");
		}
		return cpi;
	}

	public Utf8Info getUtf8Info(int index) {
		ConstantPoolInfo cpi = getConstantPoolInfo(index);
		if (cpi instanceof Utf8Info) {
			return (Utf8Info) cpi;
		}
		throw new RuntimeException(
				"The ConstantPoolInfo at the specified index " + index
						+ " is no Utf8Info but a " + cpi.getClass().getName()
						+ ".");
	}

	public static ConstantPool createConstantPool(ClassReader classStream,
			int constantPoolCount) {

		List<ConstantPoolInfo> constantPoolInfos = new ArrayList<ConstantPoolInfo>(constantPoolCount);

		for (int i = 0; i < constantPoolCount - 1; i++) {
			ConstantPoolInfo cpi = null;
			byte readTag = classStream.readByte();
			switch (readTag) {
			case CONSTANT_Class:
				cpi = ConstantPoolInfo.createConstantClassInfo(classStream);
				break;
			case CONSTANT_Fieldref:
				cpi = ConstantPoolInfo.createConstantFieldrefInfo(classStream);
				break;
			case CONSTANT_Methodref:
				cpi = ConstantPoolInfo.createConstantMethodrefInfo(classStream);
				break;
			case CONSTANT_InterfaceMethodref:
				cpi = ConstantPoolInfo
						.createInterfaceMethodrefInfo(classStream);
				break;
			case CONSTANT_String:
				cpi = ConstantPoolInfo.createStringInfo(classStream);
				break;
			case CONSTANT_Integer:
				cpi = ConstantPoolInfo.createIntegerInfo(classStream);
				break;
			case CONSTANT_Float:
				cpi = ConstantPoolInfo.createFloatInfo(classStream);
				break;
			case CONSTANT_Long:
				cpi = ConstantPoolInfo.createLongInfo(classStream);
				i++;
				break;
			case CONSTANT_Double:
				cpi = ConstantPoolInfo.createDoubleInfo(classStream);
				i++;
				break;
			case CONSTANT_NameAndType:
				cpi = ConstantPoolInfo.createNameAndTypeInfo(classStream);
				break;
			case CONSTANT_Utf8:
				cpi = ConstantPoolInfo.createUtf8Info(classStream);
				break;
			case CONSTANT_MethodHandle:
				cpi = ConstantPoolInfo.createMethodHandle(classStream);
				break;
			case CONSTANT_MethodType:
				cpi = ConstantPoolInfo.createMethodType(classStream);
				break;
			case CONSTANT_InvokeDynamic:
				cpi = ConstantPoolInfo.createInvokeDynamic(classStream);
				break;
			default:
				throw new RuntimeException(
						"unexpected constant pool info tag: " + readTag);
			}
			cpi.setTag(readTag);
			constantPoolInfos.add(i, cpi);
		}

		return new ConstantPool(constantPoolInfos);

	}

	@Override
	public Iterator<ConstantPoolInfo> iterator() {
		return new ConstantPoolIterator();
	}

	/**
	 * A special {@code Iterator} that skips the missing entries after a long
	 * and double constant.
	 *
	 * @author werner
	 *
	 */
	public class ConstantPoolIterator implements Iterator<ConstantPoolInfo> {

		int currentIndex = 0;

		@Override
		public boolean hasNext() {
			return currentIndex < constantPoolInfos.size();
		}

		@Override
		public ConstantPoolInfo next() {
			ConstantPoolInfo cpi = constantPoolInfos.get(currentIndex++);
			if (cpi == null) {
				cpi = constantPoolInfos.get(currentIndex++);
				if (cpi == null) {
					throw new IllegalStateException("Two consecutive entries in the constant pool are null.");
				}
			}
			return cpi;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
