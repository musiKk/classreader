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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;

public class ConstantPool implements Iterable<ConstantPoolInfo> {

	private final SortedMap<Integer, ConstantPoolInfo> constantPoolInfos;

	/**
	 * Maps types to the infos that are available for the type.
	 */
	private final Map<ConstantPoolInfoType, SortedMap<Integer, ConstantPoolInfo>> constantPoolInfosByType;

	private ConstantPool(SortedMap<Integer, ConstantPoolInfo> constantPoolInfos,
			Map<ConstantPoolInfoType, SortedMap<Integer, ConstantPoolInfo>> constantPoolInfosByType) {
		this.constantPoolInfos = constantPoolInfos;
		this.constantPoolInfosByType = constantPoolInfosByType;
	}

	public ConstantPoolInfo getConstantPoolInfo(int index) {
		ConstantPoolInfo cpi = constantPoolInfos.get(index - 1);
		if (cpi == null) {
			throw new IllegalArgumentException("The index " + index + " is invalid. "
					+ "Is the previous entry a long or double constant?");
		}
		return cpi;
	}

	public boolean hasInfo(ConstantPoolInfoType type, int index) {
		return constantPoolInfosByType.get(type).containsKey(index);
	}

	public ConstantPoolInfo getInfo(ConstantPoolInfoType type, int index) {
		if (!hasInfo(type, index)) {
			throw new IllegalArgumentException("There is no constant pool info of type "
					+ type + " at index " + index + ".");
		}
		return getConstantPoolInfo(index);
	}

	public List<ConstantPoolInfo> getInfos(ConstantPoolInfoType type) {
		return Collections.unmodifiableList(new ArrayList<>(constantPoolInfosByType.get(type).values()));
	}

	public Utf8Info getUtf8Info(int index) {
		return (Utf8Info) getInfo(ConstantPoolInfoType.UTF8, index);
	}

	public List<Utf8Info> getUtf8Infos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.UTF8));
	}

	public ConstantClassInfo getClassInfo(int index) {
		return (ConstantClassInfo) getInfo(ConstantPoolInfoType.CLASS, index);
	}

	public List<ConstantClassInfo> getClassInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.CLASS));
	}

	public ConstantFieldrefInfo getFieldrefInfo(int index) {
		return (ConstantFieldrefInfo) getInfo(ConstantPoolInfoType.FIELDREF, index);
	}

	public List<ConstantFieldrefInfo> getFieldrefInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.FIELDREF));
	}

	public ConstantMethodrefInfo getMethodrefInfo(int index) {
		return (ConstantMethodrefInfo) getInfo(ConstantPoolInfoType.METHODREF, index);
	}

	public List<ConstantMethodrefInfo> getMethodrefInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.METHODREF));
	}

	public InterfaceMethodrefInfo getInterfaceMethodrefInfo(int index) {
		return (InterfaceMethodrefInfo) getInfo(ConstantPoolInfoType.INTERFACE_METHODREF, index);
	}

	public List<InterfaceMethodrefInfo> getInterfaceMethodrefInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.INTERFACE_METHODREF));
	}

	public StringInfo getStringInfo(int index) {
		return (StringInfo) getInfo(ConstantPoolInfoType.STRING, index);
	}

	public List<StringInfo> getStringInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.STRING));
	}

	public IntegerInfo getIntegerInfo(int index) {
		return (IntegerInfo) getInfo(ConstantPoolInfoType.INTEGER, index);
	}

	public List<IntegerInfo> getIntegerInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.INTEGER));
	}

	public FloatInfo getFloatInfo(int index) {
		return (FloatInfo) getInfo(ConstantPoolInfoType.FLOAT, index);
	}

	public List<FloatInfo> getFloatInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.FLOAT));
	}

	public LongInfo getLongInfo(int index) {
		return (LongInfo) getInfo(ConstantPoolInfoType.LONG, index);
	}

	public List<LongInfo> getLongInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.LONG));
	}

	public DoubleInfo getDoubleInfo(int index) {
		return (DoubleInfo) getInfo(ConstantPoolInfoType.DOUBLE, index);
	}

	public List<DoubleInfo> getDoubleInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.DOUBLE));
	}

	public NameAndTypeInfo getNameAndTypeInfo(int index) {
		return (NameAndTypeInfo) getInfo(ConstantPoolInfoType.NAME_AND_TYPE, index);
	}

	public List<NameAndTypeInfo> getNameAndTypeInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.NAME_AND_TYPE));
	}

	public MethodHandleInfo getMethodHandleInfo(int index) {
		return (MethodHandleInfo) getInfo(ConstantPoolInfoType.METHOD_HANDLE, index);
	}

	public List<MethodHandleInfo> getMethodHandleInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.METHOD_HANDLE));
	}

	public MethodTypeInfo getMethodTypeInfo(int index) {
		return (MethodTypeInfo) getInfo(ConstantPoolInfoType.METHOD_TYPE, index);
	}

	public List<MethodTypeInfo> getMethodTypeInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.METHOD_TYPE));
	}

	public InvokeDynamicInfo getInvokeDynamicInfo(int index) {
		return (InvokeDynamicInfo) getInfo(ConstantPoolInfoType.INVOKE_DYNAMIC, index);
	}

	public List<InvokeDynamicInfo> getInvokeDynamicInfos() {
		return new DowncastList<>(getInfos(ConstantPoolInfoType.INVOKE_DYNAMIC));
	}

	public static ConstantPool createConstantPool(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();
		int constantPoolCount = reader.readUnsignedShort();
		SortedMap<Integer, ConstantPoolInfo> constantPoolInfos = new TreeMap<>();
		Map<ConstantPoolInfoType, SortedMap<Integer, ConstantPoolInfo>> constantPoolInfosByType = new HashMap<>();
		for (ConstantPoolInfoType t : ConstantPoolInfoType.values()) {
			constantPoolInfosByType.put(t, new TreeMap<Integer, ConstantPoolInfo>());
		}

		for (int i = 0; i < constantPoolCount - 1; i++) {
			byte tag = reader.readByte();
			ConstantPoolInfoType cpit = ConstantPoolInfoType.getByTag(tag);

			ConstantPoolInfo cpi = cpit.create(ctxt);
			cpi.setTag(tag);
			constantPoolInfos.put(i, cpi);
			constantPoolInfosByType.get(cpit).put(i + 1, cpi);
			if (cpit.isDoubleSized()) {
				i++;
			}
		}

		ConstantPool constantPool = new ConstantPool(constantPoolInfos, constantPoolInfosByType);
		ctxt.setConstantPool(constantPool);
		return constantPool;
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
			return currentIndex <= constantPoolInfos.size();
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

	/**
	 * Simple list wrapper that allows simple downcasting.
	 *
	 * @author werner
	 *
	 * @param <T>
	 */
	private static class DowncastList<T extends ConstantPoolInfo> extends AbstractList<T> {

		private final List<? super T> wrapped;

		public DowncastList(List<? super T> wrapped) {
			this.wrapped = wrapped;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T get(int index) {
			return (T) wrapped.get(index);
		}

		@Override
		public int size() {
			return wrapped.size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Iterator<T> iterator() {
			return (Iterator<T>) wrapped.iterator();
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListIterator<T> listIterator() {
			return (ListIterator<T>) wrapped.listIterator();
		}

	}

}
