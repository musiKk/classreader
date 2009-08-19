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
package classreader.attributes;

import java.util.SortedMap;
import java.util.TreeMap;

import classreader.ClassReader;
import classreader.constantpool.ConstantPool;
import classreader.instructions.Instruction;
import classreader.util.StreamUtils;

public class Code {

	private final SortedMap<Integer, Instruction> instructions;

	private Code(SortedMap<Integer, Instruction> instructions) {
		this.instructions = instructions;
	}

	public SortedMap<Integer, Instruction> getInstructions() {
		return instructions;
	}

	protected static Code getCode(byte[] code, ConstantPool constantPool) {

		ClassReader classReader = StreamUtils.createClassReader(code,
				constantPool);

		SortedMap<Integer, Instruction> instructions = new TreeMap<Integer, Instruction>();

		int currentByte = 0;

		Instruction instruction = null;
		while ((instruction = Instruction.getNextInstruction(classReader)) != null) {
			int instructionSize = instruction.getInstructionSize();
			instructions.put(currentByte, instruction);
			currentByte += instructionSize;
		}

		return new Code(instructions);
	}
}
