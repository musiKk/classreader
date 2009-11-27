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
package net.sourceforge.classreader.fields;

import net.sourceforge.classreader.ClassReader;
import net.sourceforge.classreader.attributes.Attributes;

public class FieldInfo {

	private final static int ACC_PUBLIC = 0x0001;
	private final static int ACC_PRIVATE = 0x0002;
	private final static int ACC_PROTECTED = 0x0004;
	private final static int ACC_STATIC = 0x0008;
	private final static int ACC_FINAL = 0x0010;
	private final static int ACC_VOLATILE = 0x0040;
	private final static int ACC_TRANSIENT = 0x0080;

	private final boolean _public;
	private final boolean _private;
	private final boolean _protected;
	private final boolean _static;
	private final boolean _final;
	private final boolean _volatile;
	private final boolean _transient;

	private final int nameIndex;
	private final int descriptorIndex;

	private final Attributes attributes;

	private FieldInfo(int flags, int nameIndex, int descriptorIndex,
			Attributes attributes) {

		_public = ((flags & ACC_PUBLIC) != 0);
		_private = ((flags & ACC_PRIVATE) != 0);
		_protected = ((flags & ACC_PROTECTED) != 0);
		_static = ((flags & ACC_STATIC) != 0);
		_final = ((flags & ACC_FINAL) != 0);
		_volatile = ((flags & ACC_VOLATILE) != 0);
		_transient = ((flags & ACC_TRANSIENT) != 0);

		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;

		this.attributes = attributes;

	}

	public boolean isPublic() {
		return _public;
	}

	public boolean isPrivate() {
		return _private;
	}

	public boolean isProtected() {
		return _protected;
	}

	public boolean isStatic() {
		return _static;
	}

	public boolean isFinal() {
		return _final;
	}

	public boolean isVolatile() {
		return _volatile;
	}

	public boolean isTransient() {
		return _transient;
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

	protected static FieldInfo getFieldInfo(ClassReader classStream) {

		int flags = classStream.readShort();
		int nameIndex = classStream.readShort();
		int descriptorIndex = classStream.readShort();

		int attributesCount = classStream.readShort();
		Attributes attributes = Attributes.getAttributes(classStream,
				attributesCount);

		return new FieldInfo(flags, nameIndex, descriptorIndex, attributes);

	}

}
