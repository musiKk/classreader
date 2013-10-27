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

public class Annotation {

	private final int typeIndex;
	private final ElementValuePair[] elementValuePairs;

	public Annotation(int typeIndex, ElementValuePair[] elementValuePairs) {
		this.typeIndex = typeIndex;
		this.elementValuePairs = elementValuePairs;
	}

	public static Annotation getAnnotation(ClassReader reader) {
		int typeIndex = reader.readShort();
		int numElementValuePairs = reader.readUnsignedShort();

		ElementValuePair[] elementValuePairs = new ElementValuePair[numElementValuePairs];
		for (int i = 0; i < numElementValuePairs; i++) {
			elementValuePairs[i] = ElementValuePair.getElementValuePair(reader);
		}

		return new Annotation(typeIndex, elementValuePairs);
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}

	@Override
	public String toString() {
		return "Annotation [typeIndex=" + typeIndex + ", elementValuePairs="
				+ Arrays.toString(elementValuePairs) + "]";
	}

	public static class ElementValuePair {

		private final int elementNameIndex;
		private final ElementValue elementValue;

		public ElementValuePair(int elementNameIndex, ElementValue elementValue) {
			this.elementNameIndex = elementNameIndex;
			this.elementValue = elementValue;
		}

		private static ElementValuePair getElementValuePair(ClassReader reader) {
			return new ElementValuePair(reader.readShort(), ElementValue.getElementValue(reader));
		}

		public int getElementNameIndex() {
			return elementNameIndex;
		}

		public ElementValue getElementValue() {
			return elementValue;
		}

		@Override
		public String toString() {
			return "ElementValuePair [elementNameIndex=" + elementNameIndex
					+ ", elementValue=" + elementValue + "]";
		}

	}

}
