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

import java.util.Arrays;

import com.github.musikk.classreader.ClassReader;

public abstract class ElementValue {

	private final char tag;

	public ElementValue(char tag) {
		this.tag = tag;
	}

	public static ElementValue getElementValue(ClassReader reader) {
		char tag = (char) reader.readByte();
		// TODO create some field descriptor helper or something
		switch (tag) {
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
		case 's':
			return Constant.getConstant(tag, reader);
		case 'e':
			return EnumConstant.getEnumConstant(tag, reader);
		case 'c':
			return ClassInfo.getClassInfo(tag, reader);
		case '@':
			return AnnotationValue.getAnnotationValue(tag, reader);
		case '[':
			return Array.getArray(tag, reader);
		default:
			throw new IllegalStateException("unknown tag '" + tag + "' for element value");
		}
	}

	public char getTag() {
		return tag;
	}

	public static class Constant extends ElementValue {

		private final int constValueIndex;

		private Constant(char tag, int constValueIndex) {
			super(tag);
			this.constValueIndex = constValueIndex;
		}

		private static Constant getConstant(char tag, ClassReader reader) {
			return new Constant(tag, reader.readShort());
		}

		public int getConstValueIndex() {
			return constValueIndex;
		}

		@Override
		public String toString() {
			return "Constant [constValueIndex=" + constValueIndex
					+ ", getTag()=" + getTag() + "]";
		}

	}

	public static class EnumConstant extends ElementValue {

		private final int typeNameIndex;
		private final int constNameIndex;

		private EnumConstant(char tag, int typeNameIndex, int constNameIndex) {
			super(tag);
			this.typeNameIndex = typeNameIndex;
			this.constNameIndex = constNameIndex;
		}

		private static EnumConstant getEnumConstant(char tag, ClassReader reader) {
			return new EnumConstant(tag, reader.readShort(), reader.readShort());
		}

		public int getTypeNameIndex() {
			return typeNameIndex;
		}

		public int getConstNameIndex() {
			return constNameIndex;
		}

		@Override
		public String toString() {
			return "EnumConstant [typeNameIndex=" + typeNameIndex
					+ ", constNameIndex=" + constNameIndex + ", getTag()="
					+ getTag() + "]";
		}

	}

	public static class ClassInfo extends ElementValue {

		private final int classInfoIndex;

		public ClassInfo(char tag, int classInfoIndex) {
			super(tag);
			this.classInfoIndex = classInfoIndex;
		}

		private static ClassInfo getClassInfo(char tag, ClassReader reader) {
			return new ClassInfo(tag, reader.readShort());
		}

		public int getClassInfoIndex() {
			return classInfoIndex;
		}

		@Override
		public String toString() {
			return "ClassInfo [classInfoIndex=" + classInfoIndex
					+ ", getTag()=" + getTag() + "]";
		}

	}

	public static class AnnotationValue extends ElementValue {

		private final Annotation annotation;

		private AnnotationValue(char tag, Annotation annotation) {
			super(tag);
			this.annotation = annotation;
		}

		private static AnnotationValue getAnnotationValue(char tag, ClassReader reader) {
			return new AnnotationValue(tag, Annotation.getAnnotation(reader));
		}

		public Annotation getAnnotation() {
			return annotation;
		}

		@Override
		public String toString() {
			return "AnnotationValue [annotation=" + annotation + ", getTag()="
					+ getTag() + "]";
		}

	}

	public static class Array extends ElementValue {

		private final ElementValue[] values;

		public Array(char tag, ElementValue[] values) {
			super(tag);
			this.values = values;
		}

		private static Array getArray(char tag, ClassReader reader) {
			int numValues = reader.readUnsignedShort();
			ElementValue[] values = new ElementValue[numValues];
			for (int i = 0; i < numValues; i++) {
				values[i] = ElementValue.getElementValue(reader);
			}
			return new Array(tag, values);
		}

		public ElementValue[] getValues() {
			return values;
		}

		@Override
		public String toString() {
			return "Array [values=" + Arrays.toString(values) + ", getTag()="
					+ getTag() + "]";
		}

	}

}
