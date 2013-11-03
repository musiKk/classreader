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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;

public abstract class StackMapFrame {

	private final int frameType;

	public StackMapFrame(int frameType) {
		this.frameType = frameType;
	}

	public int getFrameType() {
		return frameType;
	}

	static StackMapFrame getStackMapFrame(ClassReaderContext ctxt) {
		int frameType = ctxt.getClassReader().readUnsignedByte();
		Type type = Type.getTypeFromFrameType(frameType);

		return type.create(ctxt, frameType);
	}

	public static class SameFrame extends StackMapFrame {
		private SameFrame(int frameType) {
			super(frameType);
		}
	}

	public static class SameLocals1StackItemFrame extends StackMapFrame {
		private final VerificationTypeInfo stack;
		private SameLocals1StackItemFrame(int frameType, VerificationTypeInfo stack) {
			super(frameType);
			this.stack = stack;
		}
		public VerificationTypeInfo getStack() {
			return stack;
		}
		static SameLocals1StackItemFrame getSameLocals1StackItemFrame(ClassReaderContext ctxt, int frameType) {
			return new SameLocals1StackItemFrame(frameType, VerificationTypeInfo.getVerificationTypeInfo(ctxt));
		}
	}

	public static class SameLocals1StackItemFrameExtended extends StackMapFrame {
		private final int offsetDelta;
		private final VerificationTypeInfo stack;
		private SameLocals1StackItemFrameExtended(int frameType, int offsetDelta, VerificationTypeInfo stack) {
			super(frameType);
			this.offsetDelta = offsetDelta;
			this.stack = stack;
		}
		public int getOffsetDelta() {
			return offsetDelta;
		}
		public VerificationTypeInfo getStack() {
			return stack;
		}
		static SameLocals1StackItemFrameExtended getSameLocals1StackItemFrameExtended(ClassReaderContext ctxt, int frameType) {
			int offsetDelta = ctxt.getClassReader().readUnsignedShort();
			return new SameLocals1StackItemFrameExtended(frameType, offsetDelta, VerificationTypeInfo.getVerificationTypeInfo(ctxt));
		}
	}

	public static class ChopFrame extends StackMapFrame {
		private final int offsetDelta;
		private ChopFrame(int frameType, int offsetDelta) {
			super(frameType);
			this.offsetDelta = offsetDelta;
		}
		public int getOffsetDelta() {
			return offsetDelta;
		}
		static ChopFrame getChopFrame(ClassReaderContext ctxt, int frameType) {
			int offsetDelta = ctxt.getClassReader().readUnsignedShort();
			return new ChopFrame(frameType, offsetDelta);
		}
	}

	public static class SameFrameExtended extends StackMapFrame {
		private final int offsetDelta;
		private SameFrameExtended(int frameType, int offsetDelta) {
			super(frameType);
			this.offsetDelta = offsetDelta;
		}
		public int getOffsetDelta() {
			return offsetDelta;
		}
		static SameFrameExtended getSameFrameExtended(ClassReaderContext ctxt, int frameType) {
			int offsetDelta = ctxt.getClassReader().readUnsignedShort();
			return new SameFrameExtended(frameType, offsetDelta);
		}
	}

	public static class AppendFrame extends StackMapFrame {
		private final int offsetDelta;
		private final List<VerificationTypeInfo> locals;
		private AppendFrame(int frameType, int offsetDelta, List<VerificationTypeInfo> locals) {
			super(frameType);
			this.offsetDelta = offsetDelta;
			this.locals = locals;
		}
		public int getOffsetDelta() {
			return offsetDelta;
		}
		public List<VerificationTypeInfo> getLocals() {
			return Collections.unmodifiableList(locals);
		}
		static AppendFrame getAppendFrame(ClassReaderContext ctxt, int frameType) {
			int offsetDelta = ctxt.getClassReader().readUnsignedShort();
			int numLocals = frameType - 251;

			List<VerificationTypeInfo> locals = new ArrayList<>(numLocals);
			for (int i = 0; i < numLocals; i++) {
				locals.add(VerificationTypeInfo.getVerificationTypeInfo(ctxt));
			}

			return new AppendFrame(frameType, offsetDelta, locals);
		}
	}

	public static class FullFrame extends StackMapFrame {
		private final int offsetDelta;
		private final List<VerificationTypeInfo> locals;
		private final List<VerificationTypeInfo> stack;
		private FullFrame(int frameType, int offsetDelta, List<VerificationTypeInfo> locals, List<VerificationTypeInfo> stack) {
			super(frameType);
			this.offsetDelta = offsetDelta;
			this.locals = locals;
			this.stack = stack;
		}
		public int getOffsetDelta() {
			return offsetDelta;
		}
		public List<VerificationTypeInfo> getLocals() {
			return Collections.unmodifiableList(locals);
		}
		public List<VerificationTypeInfo> getStack() {
			return Collections.unmodifiableList(stack);
		}
		static FullFrame getFullFrame(ClassReaderContext ctxt, int frameType) {
			ClassReader reader = ctxt.getClassReader();

			int offsetDelta = reader.readUnsignedShort();
			int numLocals = reader.readUnsignedShort();

			List<VerificationTypeInfo> locals = new ArrayList<>(numLocals);
			for (int i = 0; i < numLocals; i++) {
				locals.add(VerificationTypeInfo.getVerificationTypeInfo(ctxt));
			}

			int numStackItems = reader.readUnsignedShort();

			List<VerificationTypeInfo> stack = new ArrayList<>(numStackItems);
			for (int i = 0; i < numStackItems; i++) {
				stack.add(VerificationTypeInfo.getVerificationTypeInfo(ctxt));
			}

			return new FullFrame(frameType, offsetDelta, locals, stack);
		}
	}

	public abstract static class VerificationTypeInfo {
		private final int tag;
		public VerificationTypeInfo(int tag) {
			this.tag = tag;
		}
		public int getTag() {
			return tag;
		}
		static VerificationTypeInfo getVerificationTypeInfo(ClassReaderContext ctxt) {
			ClassReader reader = ctxt.getClassReader();

			int tag = reader.readUnsignedByte();
			switch (tag) {
			case 0:
				return new TopVariableInfo(tag);
			case 1:
				return new IntegerVariableInfo(tag);
			case 2:
				return new FloatVariableInfo(tag);
			case 3:
				return new DoubleVariableInfo(tag);
			case 4:
				return new LongVariableInfo(tag);
			case 5:
				return new NullVariableInfo(tag);
			case 6:
				return new UninitializedThisVariableInfo(tag);
			case 7:
				return new ObjectVariableInfo(tag, reader.readUnsignedShort());
			case 8:
				return new UninitializedVariableInfo(tag, reader.readUnsignedShort());
			default:
				throw new IllegalArgumentException("Unknown tag " + tag + ".");
			}
		}
	}

	public static class TopVariableInfo extends VerificationTypeInfo {
		public TopVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class IntegerVariableInfo extends VerificationTypeInfo {
		public IntegerVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class FloatVariableInfo extends VerificationTypeInfo {
		public FloatVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class LongVariableInfo extends VerificationTypeInfo {
		public LongVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class DoubleVariableInfo extends VerificationTypeInfo {
		public DoubleVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class NullVariableInfo extends VerificationTypeInfo {
		public NullVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class UninitializedThisVariableInfo extends VerificationTypeInfo {
		public UninitializedThisVariableInfo(int tag) {
			super(tag);
		}
	}

	public static class ObjectVariableInfo extends VerificationTypeInfo {
		private final int constantPoolIndex;
		public ObjectVariableInfo(int tag, int constantPoolIndex) {
			super(tag);
			this.constantPoolIndex = constantPoolIndex;
		}
		public int getConstantPoolIndex() {
			return constantPoolIndex;
		}
	}

	public static class UninitializedVariableInfo extends VerificationTypeInfo {
		private final int offset;
		public UninitializedVariableInfo(int tag, int offset) {
			super(tag);
			this.offset = offset;
		}
		public int getOffset() {
			return offset;
		}
	}

	public enum Type {
		SAME_FRAME(0, 63),
		SAME_LOCALS_1_STACK_ITEM_FRAME(64, 127),
		SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED(247),
		CHOP_FRAME(248, 250),
		SAME_FRAME_EXTENDED(251),
		APPEND_FRAME(252, 254),
		FULL_FRAME(255);

		private final int frameTypeMin;
		private final int frameTypeMax;

		private Type(int frameTypeMin, int frameTypeMax) {
			this.frameTypeMin = frameTypeMin;
			this.frameTypeMax = frameTypeMax;
		}

		private Type(int frameType) {
			this(frameType, frameType);
		}

		static Type getTypeFromFrameType(int frameType) {
			for (Type t : values()) {
				if (frameType >= t.frameTypeMin && frameType <= t.frameTypeMax) {
					return t;
				}
			}
			throw new IllegalArgumentException("No stack map frame for frame type " + frameType + " found.");
		}

		StackMapFrame create(ClassReaderContext ctxt, int frameType) {
			switch (this) {
			case APPEND_FRAME:
				return AppendFrame.getAppendFrame(ctxt, frameType);
			case CHOP_FRAME:
				return ChopFrame.getChopFrame(ctxt, frameType);
			case FULL_FRAME:
				return FullFrame.getFullFrame(ctxt, frameType);
			case SAME_FRAME:
				return new SameFrame(frameType);
			case SAME_FRAME_EXTENDED:
				return SameFrameExtended.getSameFrameExtended(ctxt, frameType);
			case SAME_LOCALS_1_STACK_ITEM_FRAME:
				return SameLocals1StackItemFrame.getSameLocals1StackItemFrame(ctxt, frameType);
			case SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED:
				return SameLocals1StackItemFrameExtended.getSameLocals1StackItemFrameExtended(ctxt, frameType);
			default:
				throw new IllegalStateException();
			}
		}
	}

}
