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
package classreader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.SortedMap;

import classreader.attributes.AttributeInfo;
import classreader.attributes.Attributes;
import classreader.attributes.Code;
import classreader.attributes.CodeAttribute;
import classreader.constantpool.ConstantPool;
import classreader.constantpool.Utf8Info;
import classreader.fields.FieldInfo;
import classreader.fields.Fields;
import classreader.instructions.Instruction;
import classreader.methods.MethodInfo;
import classreader.methods.Methods;

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
	 * Bitmask for visibility modifier <i>public</i>.
	 */
	public static final int ACC_PUBLIC = 0x0001;
	/**
	 * Bitmask for modifier <i>final</i>.
	 */
	public static final int ACC_FINAL = 0x0010;
	/**
	 * Bitmask for special modifier <i>super</i>.
	 */
	public static final int ACC_SUPER = 0x0020;
	/**
	 * Bitmask for <i>interface</i> modifier.
	 */
	public static final int ACC_INTERFACE = 0x0200;
	/**
	 * Bitmask for modifier <i>abstract</i>.
	 */
	public static final int ACC_ABSTRACT = 0x0400;

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

	private boolean _public;
	private boolean _final;
	private boolean _super;
	private boolean _interface;
	private boolean _abstract;

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
		this.attributesCount = classReader.readShort();
	}

	private void readMethods() {
		this.methods = Methods.getMethods(classReader, methodsCount);
	}

	private void readMethodsCount() {
		this.methodsCount = classReader.readShort();
	}

	private void readFields() {
		this.fields = Fields.getFields(classReader, fieldsCount);
	}

	private void readFieldsCount() {
		this.fieldsCount = classReader.readShort();
	}

	private void readInterfaces() {
		this.interfaces = Interfaces
				.getInterfaces(classReader, interfacesCount);
	}

	private void readInterfacesCount() {
		this.interfacesCount = classReader.readShort();
	}

	private void readSuperClassIndex() {
		this.superClassIndex = classReader.readShort();
	}

	private void readThisClassIndex() {
		this.thisClassIndex = classReader.readShort();
	}

	/**
	 * Reads and assigns the flags for this class.
	 */
	private void readFlags() {
		int accessFlags = classReader.readShort();
		this._public = ((accessFlags & ACC_PUBLIC) != 0);
		this._final = ((accessFlags & ACC_FINAL) != 0);
		this._super = ((accessFlags & ACC_SUPER) != 0);
		this._interface = ((accessFlags & ACC_INTERFACE) != 0);
		this._abstract = ((accessFlags & ACC_ABSTRACT) != 0);
	}

	private void readConstantPool() {
		constantPool = ConstantPool.createConstantPool(classReader,
				constantPoolCount);
	}

	private void readConstantPoolCount() {
		constantPoolCount = classReader.readShort();
	}

	private void readMinor() {
		minor = classReader.readShort();
	}

	private void readMajor() {
		major = classReader.readShort();
	}

	private void readMagic() {
		this.magic = classReader.readInt();
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
		return _public;
	}

	public boolean isFinal() {
		return _final;
	}

	public boolean isSuper() {
		return _super;
	}

	public boolean isInterface() {
		return _interface;
	}

	public boolean isAbstract() {
		return _abstract;
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

		ConstantPool cp = cf.constantPool;

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