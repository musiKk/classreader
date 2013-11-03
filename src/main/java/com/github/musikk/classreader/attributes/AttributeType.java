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

import com.github.musikk.classreader.ClassReaderContext;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public enum AttributeType {

	CONSTANT_VALUE,
	CODE,
	STACK_MAP_TABLE,
	EXCEPTIONS,
	INNER_CLASSES,
	ENCLOSING_METHOD,
	SYNTHETIC,
	SIGNATURE,
	SOURCE_FILE,
	SOURCE_DEBUG_EXTENSION,
	LINE_NUMBER_TABLE,
	LOCAL_VARIABLE_TABLE,
	LOCAL_VARIABLE_TYPE_TABLE,
	DEPRECATED,
	RUNTIME_VISIBLE_ANNOTATIONS,
	RUNTIME_INVISIBLE_ANNOTATIONS,
	RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS,
	RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS,
	ANNOTATION_DEFAULT,
	BOOTSTRAP_METHODS;

	private static final BiMap<AttributeType, String> NAME_MAPPING = HashBiMap.create();

	static {
		for (AttributeType t : values()) {
			String[] nameParts = t.name().split("_");
			StringBuilder sb = new StringBuilder();
			for (String namePart : nameParts) {
				sb.append(namePart.charAt(0)).append(namePart.substring(1).toLowerCase());
			}
			NAME_MAPPING.put(t, sb.toString());
		}
	}

	public String getName() {
		return NAME_MAPPING.get(this);
	}

	public static AttributeType byName(String name) {
		return NAME_MAPPING.inverse().get(name);
	}

	public AttributeInfo create(ClassReaderContext ctxt) {
		switch (this) {
		case ANNOTATION_DEFAULT:
			return AnnotationDefault.getAnnotationDefault(ctxt);
		case BOOTSTRAP_METHODS:
			return null;
		case CODE:
			return CodeAttribute.getCode(ctxt);
		case CONSTANT_VALUE:
			return ConstantValueAttribute.getConstantValue(ctxt);
		case DEPRECATED:
			return Deprecated.getDeprecated();
		case ENCLOSING_METHOD:
			return EnclosingMethod.getEnclosingMethod(ctxt);
		case EXCEPTIONS:
			return ExceptionAttribute.getExceptionAttribute(ctxt);
		case INNER_CLASSES:
			return InnerClassesAttribute.getInnerClassesAttribute(ctxt);
		case LINE_NUMBER_TABLE:
			return LineNumberTableAttribute.getLineNumberTableAttribute(ctxt);
		case LOCAL_VARIABLE_TABLE:
			return LocalVariableTableAttribute.getLocalVariableTableAttribute(ctxt);
		case LOCAL_VARIABLE_TYPE_TABLE:
			return LocalVariableTypeTableAttribute.getLocalVariableTableAttribute(ctxt);
		case RUNTIME_INVISIBLE_ANNOTATIONS:
		case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
		case RUNTIME_VISIBLE_ANNOTATIONS:
		case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
			return null;
		case SIGNATURE:
			return Signature.getSignature(ctxt);
		case SOURCE_DEBUG_EXTENSION:
			return null;
		case SOURCE_FILE:
			return SourceFileAttribute.getSourceFileAttribute(ctxt);
		case STACK_MAP_TABLE:
			return null;
		case SYNTHETIC:
			return SyntheticAttribute.getSyntheticAttribute();
		default:
			throw new RuntimeException("Unknown attribute type " + this + ".");
		}
	}

}
