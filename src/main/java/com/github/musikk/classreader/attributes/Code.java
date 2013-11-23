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

import java.io.ByteArrayInputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.musikk.classreader.ClassReader;
import com.github.musikk.classreader.ClassReaderContext;
import com.github.musikk.classreader.ClassReaderImpl;
import com.github.musikk.classreader.instructions.Instruction;

public class Code {

	private boolean instructionsAreParsed;

	private final byte[] codeBytes;
	private final SortedMap<Integer, Instruction> instructions = new TreeMap<>();

	private Code(byte[] codeBytes) {
		this.codeBytes = codeBytes;
	}

	public SortedMap<Integer, Instruction> getInstructions() {
		if (!instructionsAreParsed) {
			readInstructions();
			instructionsAreParsed = true;
		}
		return instructions;
	}

	private void readInstructions() {
		int currentByte = 0;
		ClassReader reader = new ClassReaderImpl(new ByteArrayInputStream(codeBytes));
		while (currentByte < codeBytes.length) {
			Instruction instruction = Instruction.getNextInstruction(reader, currentByte);

			int instructionSize = instruction.getInstructionSize();
			instructions.put(currentByte, instruction);
			currentByte += instructionSize;
		}
	}

	protected static Code getCode(ClassReaderContext ctxt) {
		ClassReader reader = ctxt.getClassReader();

		int codeLength = reader.readInt();
		byte[] codeBytes = new byte[codeLength];
		reader.readBytesFully(codeBytes);

		return new Code(codeBytes);
	}
}
