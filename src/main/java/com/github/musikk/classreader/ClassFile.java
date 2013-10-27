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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.SortedMap;

import com.github.musikk.classreader.attributes.AttributeInfo;
import com.github.musikk.classreader.attributes.Attributes;
import com.github.musikk.classreader.attributes.Code;
import com.github.musikk.classreader.attributes.CodeAttribute;
import com.github.musikk.classreader.constantpool.ConstantPool;
import com.github.musikk.classreader.constantpool.ConstantPoolInfo;
import com.github.musikk.classreader.constantpool.Utf8Info;
import com.github.musikk.classreader.fields.FieldInfo;
import com.github.musikk.classreader.fields.Fields;
import com.github.musikk.classreader.instructions.Instruction;
import com.github.musikk.classreader.methods.MethodInfo;
import com.github.musikk.classreader.methods.Methods;



/**
 *
 * This class represents a class file conforming to the <a href=
 * "http://java.sun.com/docs/books/jvms/second_edition/html/VMSpecTOC.doc.html"
 * >Java Virtual Machine Specification</a>. It provides methods for every aspect
 * a class file has to offer.
 *
 * @author Werner hahn
 *
 */
public class ClassFile {

	/**
	 * The {@link ClassReader} used to read the contents of the file.
	 */
	private final ClassReader classReader;

	/**
	 * The magic number of the file. Must be <code>0xCAFEBABE</code>.
	 */
	private int magic;

	/**
	 * Minor version number.
	 */
	private int minor;
	/**
	 * Major version number.
	 */
	private int major;

	/**
	 * The number of constants as read in the file. The actual number of
	 * constants is one less.
	 */
	private int constantPoolCount;
	/**
	 * The {@link ConstantPool} of this class file.
	 */
	private ConstantPool constantPool;

	private EnumSet<Modifier> modifiers;

	/**
	 * The index of the name of this class in the {@link ClassFile#constantPool
	 * ConstantPool}.
	 */
	private int thisClassIndex;
	/**
	 * The index of the name of the super class in the
	 * {@link ClassFile#constantPool ConstantPool}.
	 */
	private int superClassIndex;

	/**
	 * The number of interfaces this class implements.
	 */
	private int interfacesCount;
	/**
	 * The interfaces this class implements.
	 */
	private Interfaces interfaces;

	/**
	 * The number of fields of this class.
	 */
	private int fieldsCount;
	/**
	 * The fields of this class.
	 */
	private Fields fields;

	/**
	 * The number of methods of this class.
	 */
	private int methodsCount;
	/**
	 * The methods of this class.
	 */
	private Methods methods;

	/**
	 * The number of attributes of this class.
	 */
	private int attributesCount;
	/**
	 * The attributes of this class.
	 */
	private Attributes attributes;

	/**
	 * Create a <code>ClassFile</code> from the given stream.
	 *
	 * @param is
	 *            an <code>InputStream</code> referring to the class.
	 */
	public ClassFile(InputStream is) {
		this.classReader = new ClassReaderImpl(is);
		parseFile();
	}

	/**
	 * Starts the parsing process. Just a bunch of delegates.
	 */
	private void parseFile() {

		readMagic();
		readMinor();
		readMajor();
		readConstantPoolCount();
		readConstantPool();
		classReader.setConstantPool(this.constantPool);
		readFlags();
		readThisClassIndex();
		readSuperClassIndex();
		readInterfacesCount();
		readInterfaces();
		readFieldsCount();
		readFields();
		readMethodsCount();
		readMethods();
		readAttributesCount();
		readAttributes();

	}

	private void readAttributes() {
		this.attributes = Attributes
				.getAttributes(classReader, attributesCount);
	}

	private void readAttributesCount() {
		this.attributesCount = classReader.readUnsignedShort();
	}

	private void readMethods() {
		this.methods = Methods.getMethods(classReader, methodsCount);
	}

	private void readMethodsCount() {
		this.methodsCount = classReader.readUnsignedShort();
	}

	private void readFields() {
		this.fields = Fields.getFields(classReader, fieldsCount);
	}

	private void readFieldsCount() {
		this.fieldsCount = classReader.readUnsignedShort();
	}

	private void readInterfaces() {
		this.interfaces = Interfaces
				.getInterfaces(classReader, interfacesCount);
	}

	private void readInterfacesCount() {
		this.interfacesCount = classReader.readUnsignedShort();
	}

	private void readSuperClassIndex() {
		this.superClassIndex = classReader.readUnsignedShort();
	}

	private void readThisClassIndex() {
		this.thisClassIndex = classReader.readUnsignedShort();
	}

	/**
	 * Reads and assigns the flags for this class.
	 */
	private void readFlags() {
		int accessFlags = classReader.readShort();
		modifiers = Modifier.readModifiers(accessFlags, Modifier.Target.CLASS);
	}

	private void readConstantPool() {
		constantPool = ConstantPool.createConstantPool(classReader,
				constantPoolCount);
	}

	private void readConstantPoolCount() {
		constantPoolCount = classReader.readUnsignedShort();
	}

	private void readMinor() {
		minor = classReader.readUnsignedShort();
	}

	private void readMajor() {
		major = classReader.readUnsignedShort();
	}

	private void readMagic() {
		this.magic = classReader.readInt();
		if (this.magic != 0xCAFEBABE) {
			throw new RuntimeException("magic number is not 0xCAFEBABE but 0x"
					+ Integer.toString(this.magic, 16));
		}
	}

	public int getMagic() {
		return magic;
	}

	public int getMinor() {
		return minor;
	}

	public int getMajor() {
		return major;
	}

	public int getConstantPoolCount() {
		return constantPoolCount;
	}

	public ConstantPool getConstantPool() {
		return constantPool;
	}

	public boolean isPublic() {
		return modifiers.contains(Modifier.PUBLIC);
	}

	public boolean isFinal() {
		return modifiers.contains(Modifier.FINAL);
	}

	public boolean isSuper() {
		return modifiers.contains(Modifier.SUPER);
	}

	public boolean isInterface() {
		return modifiers.contains(Modifier.INTERFACE);
	}

	public boolean isAbstract() {
		return modifiers.contains(Modifier.ABSTRACT);
	}

	public int getThisClassIndex() {
		return thisClassIndex;
	}

	public int getSuperClassIndex() {
		return superClassIndex;
	}

	public int getInterfacesCount() {
		return interfacesCount;
	}

	public Interfaces getInterfaces() {
		return interfaces;
	}

	public int getFieldsCount() {
		return fieldsCount;
	}

	public Fields getFields() {
		return fields;
	}

	public int getMethodsCount() {
		return methodsCount;
	}

	public Methods getMethods() {
		return methods;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * A simple {@code main} method to test the Classreader library. It reads a
	 * single class file and dumps a few statistics to standard output.
	 *
	 * @param args
	 *            the first element of this array is treated as a path to a
	 *            class file, the rest is ignored
	 * @throws FileNotFoundException
	 *             if the path does not point to an existing file
	 * @throws NullPointerException
	 *             if no argument is provided
	 */
	public static void main(String[] args) throws FileNotFoundException {

		String classFilePath = args[0];

		File classFile = new File(classFilePath);
		FileInputStream fis = new FileInputStream(classFile);

		ClassFile cf = new ClassFile(new BufferedInputStream(fis));

		System.out.printf("magic: %X%n", cf.getMagic());
		System.out.printf("minor: %d%n", cf.getMinor());
		System.out.printf("major: %d%n", cf.getMajor());
		System.out.printf("const: %d%n", cf.getConstantPoolCount());

		System.out.printf("public:    %b%n", cf.isPublic());
		System.out.printf("final:     %b%n", cf.isFinal());
		System.out.printf("super:     %b%n", cf.isSuper());
		System.out.printf("interface: %b%n", cf.isInterface());
		System.out.printf("abstract:  %b%n", cf.isAbstract());

		System.out.println();

		ConstantPool cp = cf.constantPool;

		System.out.println("constant pool:");
		for (int i = 1; i <= cf.constantPoolCount; i++) {
			ConstantPoolInfo info = cp.getConstantPoolInfo(i);
			System.out.printf(" - %d -> %s%n", i, info);
		}
		System.out.println(cf.constantPoolCount + " items\n");

		Fields fields = cf.getFields();
		System.out.println("fields:");
		for (FieldInfo fi : fields.getFieldInfos()) {
			int nameIndex = fi.getNameIndex();
			int descriptorIndex = fi.getDescriptorIndex();

			System.out.printf("  name:       %d (%s)%n", nameIndex,
					((Utf8Info) cp.getConstantPoolInfo(nameIndex)).getValue());
			System.out.printf("  descriptor: %d (%s)%n", descriptorIndex,
					((Utf8Info) cp.getConstantPoolInfo(descriptorIndex))
							.getValue());
			System.out.printf("  public:     %b%n", fi.isPublic());
			System.out.printf("  final:      %b%n", fi.isFinal());
			System.out.printf("  private:    %b%n", fi.isPrivate());
			System.out.printf("  protected:  %b%n", fi.isProtected());
			System.out.printf("  public:     %b%n", fi.isPublic());
			System.out.printf("  static:     %b%n", fi.isStatic());
			System.out.printf("  transient:  %b%n", fi.isTransient());
			System.out.printf("  volatile:   %b%n", fi.isVolatile());

			System.out.println("  attributes:");
			Attributes attributes = fi.getAttributes();
			for (AttributeInfo ai : attributes.getAttributeInfos()) {
				int attributeNameIndex = ai.getAttributeNameIndex();
				System.out.printf("    name: %d (%s)%n", attributeNameIndex,
						((Utf8Info) cp.getConstantPoolInfo(attributeNameIndex))
								.getValue());
				System.out.printf("    info: %s%n", Arrays.toString(ai
						.getInfo()));
				System.out.printf("    to string: %s%n", ai);
				System.out.println();
			}

			System.out.println();
		}

		Methods methods = cf.getMethods();
		System.out.println("methods:");
		for (MethodInfo mi : methods.getMethodInfos()) {
			int nameIndex = mi.getNameIndex();
			int descriptorIndex = mi.getDescriptorIndex();

			System.out.printf("  name:         %d (%s)%n", nameIndex,
					((Utf8Info) cp.getConstantPoolInfo(nameIndex)).getValue());
			System.out.printf("  descriptor:   %d (%s)%n", descriptorIndex,
					((Utf8Info) cp.getConstantPoolInfo(descriptorIndex))
							.getValue());
			System.out.printf("  public:       %b%n", mi.isPublic());
			System.out.printf("  final:        %b%n", mi.isFinal());
			System.out.printf("  private:      %b%n", mi.isPrivate());
			System.out.printf("  protected:    %b%n", mi.isProtected());
			System.out.printf("  public:       %b%n", mi.isPublic());
			System.out.printf("  static:       %b%n", mi.isStatic());
			System.out.printf("  abstract:     %b%n", mi.isAbstract());
			System.out.printf("  native:       %b%n", mi.isNative());
			System.out.printf("  strict:       %b%n", mi.isStrict());
			System.out.printf("  synchronized: %b%n", mi.isSynchronized());

			System.out.println("  attributes:");
			Attributes attributes = mi.getAttributes();
			for (AttributeInfo ai : attributes.getAttributeInfos()) {
				int attributeNameIndex = ai.getAttributeNameIndex();
				String name = ((Utf8Info) cp
						.getConstantPoolInfo(attributeNameIndex)).getValue();
				System.out.printf("    name: %d (%s)%n", attributeNameIndex,
						name);
				System.out.printf("    info: %s%n", Arrays.toString(ai
						.getInfo()));
				System.out.printf("    to string: %s%n", ai);

				if ("Code".equals(name)) {
					CodeAttribute codeAttribute = (CodeAttribute) ai;
					Code code = codeAttribute.getCode();
					SortedMap<Integer, Instruction> instructions = code
							.getInstructions();
					for (Integer byteOffset : instructions.keySet()) {
						System.out.printf("    %4d: %s%n", byteOffset,
								instructions.get(byteOffset).toString());
					}
				}

				System.out.println();
			}

			System.out.println();
		}

		Attributes attributes = cf.getAttributes();
		for (AttributeInfo ai : attributes.getAttributeInfos()) {
			int attributeNameIndex = ai.getAttributeNameIndex();
			System.out.printf("name: %d (%s)%n", attributeNameIndex,
					((Utf8Info) cp.getConstantPoolInfo(attributeNameIndex))
							.getValue());
			System.out.printf("info: %s%n", Arrays.toString(ai.getInfo()));
			System.out.println();
		}

	}

}
