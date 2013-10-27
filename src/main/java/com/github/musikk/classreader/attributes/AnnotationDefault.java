package com.github.musikk.classreader.attributes;

import com.github.musikk.classreader.constantpool.ConstantPool;
import com.github.musikk.classreader.util.StreamUtils;

public class AnnotationDefault extends AttributeInfo {

	private final ElementValue defaultValue;

	public AnnotationDefault(ElementValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	public static AttributeInfo getAnnotationDefault(int attributeLength, byte[] info, ConstantPool constantPool) {
		return new AnnotationDefault(ElementValue.getElementValue(StreamUtils.createClassReader(info, constantPool)));
	}

	public ElementValue getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return "AnnotationDefault [defaultValue=" + defaultValue + "]";
	}

}
