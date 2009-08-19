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

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

public class InstructionProperties extends Properties implements
		Iterable<InstructionProperty>, Iterator<InstructionProperty> {

	private final static String INSTRUCTION_PROPERTIES_PATH = "/resources/config/instructions.properties";

	private final static String INSTRUCTION_COUNT_KEY = "instruction.count";

	private final static String INSTRUCTION_BASE_KEY = "instruction.%d.";
	private final static String INSTRUCTION_OPCODE_KEY;
	private final static String INSTRUCTION_MNEMONIC_KEY;
	private final static String INSTRUCTION_SIZE_KEY;

	private final static String INSTRUCTION_OPERANDS_BASE_KEY = INSTRUCTION_BASE_KEY
			+ "operands.";
	private final static String INSTRUCTION_OPERANDS_TYPES_KEY;
	private final static String INSTRUCTION_OPERANDS_NAMES_KEY;

	static {
		INSTRUCTION_OPCODE_KEY = INSTRUCTION_BASE_KEY + "opcode";
		INSTRUCTION_MNEMONIC_KEY = INSTRUCTION_BASE_KEY + "mnemonic";
		INSTRUCTION_SIZE_KEY = INSTRUCTION_BASE_KEY + "size";

		INSTRUCTION_OPERANDS_TYPES_KEY = INSTRUCTION_OPERANDS_BASE_KEY
				+ "types";
		INSTRUCTION_OPERANDS_NAMES_KEY = INSTRUCTION_OPERANDS_BASE_KEY
				+ "names";
	}

	private int currentIndex;
	private final int maxIndex;

	public InstructionProperties() {

		try {
			this.load(InstructionReader.class
					.getResourceAsStream(INSTRUCTION_PROPERTIES_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.currentIndex = 1;
		this.maxIndex = Integer.parseInt(getProperty(INSTRUCTION_COUNT_KEY));

	}

	@Override
	public Iterator<InstructionProperty> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return currentIndex <= maxIndex;
	}

	@Override
	public InstructionProperty next() {
		return getInstructionProperty(currentIndex++);
	}

	public InstructionProperty getInstructionProperty(int index) {

		int opcode = getOpcode(getProperty(String.format(
				INSTRUCTION_OPCODE_KEY, index)));
		String mnemonic = getProperty(String.format(INSTRUCTION_MNEMONIC_KEY,
				index));
		int size = getSize(getProperty(String.format(INSTRUCTION_SIZE_KEY,
				index)));

		String typesProperty = getProperty(String.format(
				INSTRUCTION_OPERANDS_TYPES_KEY, index));
		String[] types = null;
		if (typesProperty != null) {
			types = typesProperty.split(",");
		}
		String namesProperty = getProperty(String.format(
				INSTRUCTION_OPERANDS_NAMES_KEY, index));
		String[] names = null;
		if (namesProperty != null) {
			names = namesProperty.split(",");
		}

		return new InstructionProperty(opcode, mnemonic, size, types, names);

	}

	private int getSize(String sizeString) {

		if (sizeString == null) {
			return 1;
		}
		if (sizeString.equals("v")) {
			return -1;
		} else {
			return Integer.parseInt(sizeString);
		}

	}

	private static int getOpcode(String opcodeString) {

		if (opcodeString.startsWith("0x")) {
			return Integer.parseInt(opcodeString.substring(2), 16);
		} else {
			return Integer.parseInt(opcodeString);
		}

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(
				"Cannot remove something from a properties file.");
	}

	public int getNumberOfInstructions() {
		return maxIndex;
	}

	private static final long serialVersionUID = 1381504806580178143L;

}
