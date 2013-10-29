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

import com.github.musikk.classreader.ClassReaderContext;

public enum ConstantPoolInfoType {

	CLASS(7),
	FIELDREF(9),
	METHODREF(10),
	INTERFACE_METHODREF(11),
	STRING(8),
	INTEGER(3),
	FLOAT(4),
	LONG(5),
	DOUBLE(6),
	NAME_AND_TYPE(12),
	UTF8(1),
	METHOD_HANDLE(15),
	METHOD_TYPE(16),
	INVOKE_DYNAMIC(18);

	private byte value;

	private ConstantPoolInfoType(int value) {
		this.value = (byte) value;
	}

	public byte getValue() {
		return value;
	}

	public static ConstantPoolInfoType getByTag(byte tag) {
		for (ConstantPoolInfoType t : values()) {
			if (t.value == tag) {
				return t;
			}
		}
		throw new IllegalArgumentException("unknown tag " + (tag & 0xFF));
	}

	public ConstantPoolInfo create(ClassReaderContext ctxt) {
		switch(this) {
		case CLASS:
			return ConstantClassInfo.createConstantClassInfo(ctxt);
		case DOUBLE:
			return DoubleInfo.createDoubleInfo(ctxt);
		case FIELDREF:
			return ConstantFieldrefInfo.createConstantFieldrefInfo(ctxt);
		case FLOAT:
			return FloatInfo.createFloatInfo(ctxt);
		case INTEGER:
			return IntegerInfo.createIntegerInfo(ctxt);
		case INTERFACE_METHODREF:
			return ConstantMethodrefInfo.createConstantMethodrefInfo(ctxt);
		case INVOKE_DYNAMIC:
			return InvokeDynamicInfo.createInvokeDynamicInfo(ctxt);
		case LONG:
			return LongInfo.createLongInfo(ctxt);
		case METHODREF:
			return ConstantMethodrefInfo.createConstantMethodrefInfo(ctxt);
		case METHOD_HANDLE:
			return MethodHandleInfo.createMethodHandleInfo(ctxt);
		case METHOD_TYPE:
			return MethodTypeInfo.createMethodTypeInfo(ctxt);
		case NAME_AND_TYPE:
			return NameAndTypeInfo.createNameAndTypeInfo(ctxt);
		case STRING:
			return StringInfo.createStringInfo(ctxt);
		case UTF8:
			return Utf8Info.createUtf8Info(ctxt);
		default:
			throw new IllegalStateException("missing type " + this.name());
		}
	}

}
