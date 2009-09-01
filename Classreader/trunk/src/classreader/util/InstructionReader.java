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
package classreader.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classreader.ClassReader;
import classreader.instructions.Instruction;
import classreader.instructions.InstructionFactory;
import classreader.instructions.Operand;

public class InstructionReader {

	private final static int OPCODE_LOOKUPSWITCH = 0xab;
	private final static int OPCODE_TABLESWITCH = 0xaa;
	private final static int OPCODE_WIDE = 0xc4;

	private final static List<Integer> OPCODE_WIDE_FORMAT_1;
	private final static List<Integer> OPCODE_WIDE_FORMAT_2;

	static {
		OPCODE_WIDE_FORMAT_1 = Arrays.asList(0x15 /* iload */, 0x18 /* fload */,
				0x19 /* aload */, 0x16 /* lload */, 0x18 /* dload */,
				0x36 /* istore */, 0x38 /* fstore */, 0x3a /* astore */,
				0x37 /* lstore */, 0x39 /* dstore */, 0xa9 /* ret */);
		OPCODE_WIDE_FORMAT_2 = Arrays.asList(0x84 /* iinc */);
	}

	public static Map<Integer, InstructionFactory> loadInstructionFactories() {

		InstructionProperties instructionProperties = new InstructionProperties();

		Map<Integer, InstructionFactory> instructionFactories = new HashMap<Integer, InstructionFactory>(
				instructionProperties.getNumberOfInstructions());

		for (InstructionProperty instructionProperty : instructionProperties) {

			int opcode = instructionProperty.getOpcode();
			String mnemonic = instructionProperty.getMnemonic();
			int instructionSize = instructionProperty.getSize();

			String[] types = instructionProperty.getTypes();
			String[] names = instructionProperty.getNames();

			InstructionFactory instructionFactory = null;

			if (instructionSize == -1) {
				instructionFactory = createSpecialInstructionFactory(opcode,
						mnemonic);
			} else {
				instructionFactory = createRegularInstructionFactory(opcode,
						mnemonic, instructionSize, types, names);
			}

			instructionFactories.put(Integer.valueOf(opcode),
					instructionFactory);

		}

		return instructionFactories;

	}

	private static InstructionFactory createLookupswitchInstructionFactory(
			final int opcode, final String mnemonic) {
		return new InstructionFactory() {

			@Override
			public Instruction getInstruction(ClassReader classReader) {

				int size = 1;

				int padding = ensureZeroPadding(classReader, 4);
				size += padding;

				List<Operand> operands = new ArrayList<Operand>();

				int _default = classReader.readInt();
				int npairs = classReader.readInt();
				size += 8;

				operands.add(new Operand("default", _default));
				operands.add(new Operand("npairs", npairs));

				for (int i = 0; i < npairs; i++) {
					int match = classReader.readInt();
					int offset = classReader.readInt();
					operands.add(new Operand("match", match));
					operands.add(new Operand("offset", offset));
					size += 8;
				}

				return new Instruction(opcode, mnemonic, size, operands
						.toArray(new Operand[] {}));
			}
		};
	}

	private static InstructionFactory createTableswitchInstructionFactory(
			final int opcode, final String mnemonic) {
		return new InstructionFactory() {

			@Override
			public Instruction getInstruction(ClassReader classReader) {

				int size = 1;

				int padding = ensureZeroPadding(classReader, 4);
				size += padding;

				List<Operand> operands = new ArrayList<Operand>();

				int defaultByte = classReader.readInt();
				int lowByte = classReader.readInt();
				int highByte = classReader.readInt();

				operands.add(new Operand("defaultbyte", defaultByte));
				operands.add(new Operand("lowByte", lowByte));
				operands.add(new Operand("highByte", highByte));
				size += 12;

				for (int i = 0; i < highByte - lowByte + 1; i++) {
					int offset = classReader.readInt();
					operands.add(new Operand("offset", offset));
					size += 4;
				}

				return new Instruction(opcode, mnemonic, size, operands
						.toArray(new Operand[] {}));
			}
		};
	}

	private static InstructionFactory createWideInstructionFactory(
			final int opcode, final String mnemonic) {
		return new InstructionFactory() {

			@Override
			public Instruction getInstruction(ClassReader classReader) {

				int size = 1;

				List<Operand> operands = new ArrayList<Operand>();

				int affectedOpcode = classReader.readUnsignedByte();

				int index = classReader.readShort();

				operands.add(new Operand("opcode", affectedOpcode));
				size++;
				operands.add(new Operand("index", index));
				size += 2;

				if (OPCODE_WIDE_FORMAT_1.contains(affectedOpcode)) {
					// need nothing more
				} else if (OPCODE_WIDE_FORMAT_2.contains(affectedOpcode)) {
					int constant = classReader.readShort();
					operands.add(new Operand("constant", constant));
					size += 2;
				} else {
					throw new RuntimeException(
							"illegal opcode for wide instruction: " + opcode);
				}

				return new Instruction(opcode, mnemonic, size, operands
						.toArray(new Operand[] {}));
			}
		};
	}

	private static InstructionFactory createSpecialInstructionFactory(
			int opcode, String mnemonic) {

		switch (opcode) {
		case OPCODE_LOOKUPSWITCH:
			return createLookupswitchInstructionFactory(opcode, mnemonic);
		case OPCODE_TABLESWITCH:
			return createTableswitchInstructionFactory(opcode, mnemonic);
		case OPCODE_WIDE:
			return createWideInstructionFactory(opcode, mnemonic);
		default:
			throw new RuntimeException("illegal opcode " + opcode);
		}

	}

	private static int ensureZeroPadding(ClassReader classReader, int alignment) {

		int position = classReader.getPosition();
		int padding = 0;

		if (position % alignment == 0) {
			padding = 0;
		} else {
			padding = alignment - (position % alignment);
		}

		for (int i = 0; i < padding; i++) {
			byte b = classReader.readByte();
			if (b != 0) {
				throw new RuntimeException("padding byte must be zero but was "
						+ b);
			}
		}

		return padding;

	}

	private static InstructionFactory createRegularInstructionFactory(
			final int opcode, final String mnemonic, final int instructionSize,
			final String[] types, final String[] names) {

		return new InstructionFactory() {
			@Override
			public Instruction getInstruction(ClassReader classReader) {
				return new Instruction(opcode, mnemonic, instructionSize,
						getOperands(types, names, classReader));
			}
		};

	}

	private static Operand[] getOperands(String[] types, String[] names,
			ClassReader classReader) {

		if (types == null) {
			return new Operand[] {};
		}

		List<Operand> operands = new ArrayList<Operand>();

		for (int i = 0; i < types.length; i++) {
			Operand operand = getOperand(types[i], names[i], classReader);
			if (operand == null) {
				continue;
			} else {
				operands.add(operand);
			}
		}

		return operands.toArray(new Operand[] {});

	}

	private static Operand getOperand(String type, String name,
			ClassReader classReader) {

		int value = 0;
		char typeChar = type.charAt(0);

		switch (typeChar) {
		case 'b':
			value = classReader.readByte();
			break;
		case 'B':
			value = classReader.readUnsignedByte();
			break;
		case 's':
			value = classReader.readShort();
			break;
		case 'S':
			value = classReader.readUnsignedShort();
			break;
		case 'i':
			value = classReader.readInt();
			break;
		case 'c':
		case 'C':
			if (classReader.readByte() != Integer.parseInt(name)) {
				throw new RuntimeException("expected constant value " + name
						+ " but found " + value + " instead");
			}
			return null;
		default:
			throw new RuntimeException("unexpected type: " + type);
		}

		return new Operand(name, value);

	}

}
