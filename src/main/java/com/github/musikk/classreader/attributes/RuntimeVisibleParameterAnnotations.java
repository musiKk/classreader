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
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class RuntimeVisibleParameterAnnotations extends AttributeInfo {

	private final Multimap<Integer, Annotation> parameterAnnotations;

	public RuntimeVisibleParameterAnnotations(Multimap<Integer, Annotation> parameterAnnotations) {
		this.parameterAnnotations = parameterAnnotations;
	}

	public Multimap<Integer, Annotation> getParameterAnnotations() {
		return Multimaps.unmodifiableMultimap(parameterAnnotations);
	}

	static RuntimeVisibleParameterAnnotations getRuntimeVisibleParameterAnnotations(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int numParameters = reader.readUnsignedByte();
		Multimap<Integer, Annotation> parameterAnnotations = LinkedListMultimap.create();
		for (int i = 0; i < numParameters; i++) {
			int numAnnotations = reader.readUnsignedShort();
			for (int j = 0; j < numAnnotations; j++) {
				parameterAnnotations.put(i, Annotation.getAnnotation(ctxt));
			}
		}

		return new RuntimeVisibleParameterAnnotations(parameterAnnotations);
	}

}
