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
package com.github.musikk.classreader.attributes;

import com.github.musikk.classreader.ClassFile;
import com.github.musikk.classreader.ClassReader;

public class InnerClass {

	private final int innerClassInfoIndex;
	private final int outerClassInfoIndex;
	private final int innerNameIndex;

	private final boolean _public;
	private final boolean _final;
	private final boolean _super;
	private final boolean _interface;
	private final boolean _abstract;

	private InnerClass(int innerClassInfoIndex, int outerClassInfoIndex,
			int innerNameIndex, int innerClassAccessFlags) {

		this.innerClassInfoIndex = innerClassInfoIndex;
		this.outerClassInfoIndex = outerClassInfoIndex;
		this.innerNameIndex = innerNameIndex;

		this._public = ((innerClassAccessFlags & ClassFile.ACC_PUBLIC) != 0);
		this._final = ((innerClassAccessFlags & ClassFile.ACC_FINAL) != 0);
		this._super = ((innerClassAccessFlags & ClassFile.ACC_SUPER) != 0);
		this._interface = ((innerClassAccessFlags & ClassFile.ACC_INTERFACE) != 0);
		this._abstract = ((innerClassAccessFlags & ClassFile.ACC_ABSTRACT) != 0);

	}

	public int getInnerClassInfoIndex() {
		return innerClassInfoIndex;
	}

	public int getOuterClassInfoIndex() {
		return outerClassInfoIndex;
	}

	public int getInnerNameIndex() {
		return innerNameIndex;
	}

	public boolean is_public() {
		return _public;
	}

	public boolean is_final() {
		return _final;
	}

	public boolean is_super() {
		return _super;
	}

	public boolean is_interface() {
		return _interface;
	}

	public boolean is_abstract() {
		return _abstract;
	}

	protected static InnerClass getInnerClass(ClassReader classReader) {

		int innerClassInfoIndex = classReader.readShort();
		int outerClassInfoIndex = classReader.readShort();
		int innerNameIndex = classReader.readShort();
		int innerClassAccessFlags = classReader.readShort();

		return new InnerClass(innerClassInfoIndex, outerClassInfoIndex,
				innerNameIndex, innerClassAccessFlags);

	}

}
