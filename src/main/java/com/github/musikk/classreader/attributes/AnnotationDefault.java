package com.github.musikk.classreader.attributes;

import com.github.musikk.classreader.ClassReaderContext;

public class AnnotationDefault extends AttributeInfo {

	private final ElementValue defaultValue;

	public AnnotationDefault(ElementValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	public static AttributeInfo getAnnotationDefault(ClassReaderContext ctxt) {
		return new AnnotationDefault(ElementValue.getElementValue(ctxt));
	}

	public ElementValue getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return "AnnotationDefault [defaultValue=" + defaultValue + "]";
	}

}
