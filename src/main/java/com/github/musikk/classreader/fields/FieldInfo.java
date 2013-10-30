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
package com.github.musikk.classreader.fields;

import java.util.EnumSet;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;
import com.github.musikk.classreader.Modifier;
import com.github.musikk.classreader.attributes.Attributes;

public class FieldInfo {

	private final EnumSet<Modifier> modifiers;

	private final int nameIndex;
	private final int descriptorIndex;

	private final Attributes attributes;

	private FieldInfo(int flags, int nameIndex, int descriptorIndex,
			Attributes attributes) {

		modifiers = Modifier.readModifiers(flags, Modifier.Target.FIELD);

		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;

		this.attributes = attributes;

	}

	public boolean isPublic() {
		return modifiers.contains(Modifier.PUBLIC);
	}

	public boolean isPrivate() {
		return modifiers.contains(Modifier.PRIVATE);
	}

	public boolean isProtected() {
		return modifiers.contains(Modifier.PROTECTED);
	}

	public boolean isStatic() {
		return modifiers.contains(Modifier.STATIC);
	}

	public boolean isFinal() {
		return modifiers.contains(Modifier.FINAL);
	}

	public boolean isVolatile() {
		return modifiers.contains(Modifier.VOLATILE);
	}

	public boolean isTransient() {
		return modifiers.contains(Modifier.TRANSIENT);
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	protected static FieldInfo getFieldInfo(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int flags = reader.readShort();
		int nameIndex = reader.readUnsignedShort();
		int descriptorIndex = reader.readUnsignedShort();

		Attributes attributes = Attributes.getAttributes(ctxt);

		return new FieldInfo(flags, nameIndex, descriptorIndex, attributes);

	}

}
