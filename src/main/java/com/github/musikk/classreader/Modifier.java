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
package com.github.musikk.classreader;

import java.util.EnumSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public enum Modifier {

	PUBLIC(0x0001, Target.CLASS, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	PRIVATE(0x0002, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	PROTECTED(0x0004, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	STATIC(0x0008, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	FINAL(0x0010, Target.CLASS, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	SUPER(0x0020, Target.CLASS),
	SYNCHRONIZED(0x0020, Target.METHOD),
	VOLATILE(0x0040, Target.FIELD),
	BRIDGE(0x0040, Target.METHOD),
	TRANSIENT(0x0080, Target.FIELD),
	VARARGS(0x0080, Target.METHOD),
	NATIVE(0x0100, Target.METHOD),
	INTERFACE(0x0200, Target.CLASS, Target.NESTED_CLASS),
	ABSTRACT(0x0400, Target.CLASS, Target.METHOD, Target.NESTED_CLASS),
	STRICT(0x0800, Target.METHOD),
	SYNTHETIC(0x1000, Target.CLASS, Target.FIELD, Target.METHOD, Target.NESTED_CLASS),
	ANNOTATION(0x2000, Target.CLASS, Target.NESTED_CLASS),
	ENUM(0x4000, Target.CLASS, Target.FIELD, Target.NESTED_CLASS);

	private final int bitmask;
	private final Target[] targets;

	private static final Multimap<Target, Modifier> MODIFIERS_PER_TARGET = HashMultimap.create(4, 8);

	static {
		for (Modifier m : values()) {
			for (Target t : m.targets) {
				MODIFIERS_PER_TARGET.put(t, m);
			}
		}
	}

	private Modifier(int bitmask, Target ... targets) {
		this.bitmask = bitmask;
		this.targets = targets;
	}

	public enum Target {
		CLASS, FIELD, METHOD, NESTED_CLASS
	}

	public static EnumSet<Modifier> readModifiers(int accessFlags, Target target) {
		EnumSet<Modifier> s = EnumSet.noneOf(Modifier.class);
		for (Modifier candidate : MODIFIERS_PER_TARGET.get(target)) {
			if ((candidate.bitmask & accessFlags) != 0) {
				s.add(candidate);
			}
		}
		return s;
	}

}
