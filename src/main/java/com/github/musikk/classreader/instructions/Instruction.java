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
package com.github.musikk.classreader.instructions;

import java.util.Arrays;
import java.util.Map;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.util.InstructionReader;

public class Instruction {

	private static final Map<Integer, InstructionFactory> instructionFactories;

	private final int opcode;
	private final String mnemonic;
	private final int instructionSize;
	private final Operand[] operands;

	public Instruction(int opcode, String mnemonic, int instructionSize,
			Operand[] operands) {
		this.opcode = opcode;
		this.mnemonic = mnemonic;
		this.instructionSize = instructionSize;
		this.operands = operands != null ? operands : new Operand[] {};
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public Operand[] getOperands() {
		return operands;
	}

	public int getOpcode() {
		return opcode;
	}

	/**
	 * Queries the size of the instruction in bytes. The size is the size of the
	 * instruction itself plus all operands.
	 *
	 * @return the size of the instruction in bytes
	 */
	public int getInstructionSize() {
		return instructionSize;
	}

	public static Instruction getNextInstruction(ClassReader classReader, int methodOffset) {

		int opcode = -1;
		try {
			opcode = classReader.readUnsignedByte();
		} catch (RuntimeException e) {
			return null;
		}
		InstructionFactory instructionFactory = instructionFactories.get(Integer.valueOf(opcode));

		if (instructionFactory == null) {
			throw new RuntimeException(String.format("illegal opcode: 0x%x", opcode));
		}

		Instruction instruction = instructionFactory.getInstruction(classReader, methodOffset);

		return instruction;

	}

	@Override
	public String toString() {
		return String.format("0x%x[%s]%s", opcode, mnemonic,
				operands.length > 0 ? Arrays.toString(operands) : "");
	}

	static {
		instructionFactories = InstructionReader.loadInstructionFactories();
	}

}
