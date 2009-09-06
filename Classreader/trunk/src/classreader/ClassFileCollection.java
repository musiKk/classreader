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

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * A {@code ClassFileCollection} is an arbitrary collection of classes.
 * 
 * @author Werner Hahn
 * 
 */
public abstract class ClassFileCollection {

	/**
	 * Returns the {@link ClassFile} with the given name. The name of a class is
	 * its fully qualified name, so for example the class name of the class
	 * {@code Foo} in the package {@code bar.baz} is {@code bar.baz.Foo}.
	 * 
	 * @param className
	 *            the name of the class
	 * @return the created {@code ClassFile} or {@code null} if no class with
	 *         the given name exists
	 */
	public abstract ClassFile getClassFile(String className);

	/**
	 * Returns a {@link List} of class names this {@code ClassFileCollection}
	 * has to offer (optional operation).
	 * 
	 * @return the {@code List} of class names
	 * @throws UnsupportedOperationException
	 *             if this {@code ClassFileCollection} maintains no such
	 *             information
	 */
	public Collection<String> getClassNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a lazy {@code ClassFileCollection} from the given JAR
	 * {@link File}. Same as
	 * {@linkplain ClassFileCollection#getClassFileCollection(File, ClassFileJarMode)
	 * getClassFileCollection(jar, ClassFileJarMode.LAZY)}.
	 * 
	 * @param jar
	 *            the {@code File} to load
	 * @return the {@code ClassFileCollection} with lazy access to the classes
	 *         contained in the given JAR {code File}
	 * @see ClassFileCollection#getClassFileCollection(File, ClassFileJarMode)
	 */
	public static ClassFileCollection getClassFileCollection(File jar) {
		return getClassFileCollection(jar, ClassFileJarMode.LAZY);
	}

	/**
	 * Creates a {@code ClassFileCollection} from the given JAR {@link File}
	 * with the specified mode.
	 * 
	 * @param jar
	 *            the {@code File} to load
	 * @param mode
	 *            the mode with which to load the JAR {@code File}
	 * @return the {@code ClassFileCollection} with access to the classes
	 *         contained in the given JAR {code File}
	 */
	public static ClassFileCollection getClassFileCollection(File jar,
			ClassFileJarMode mode) {
		switch (mode) {
		case EAGER:
			return new EagerClassFileJar(jar);
		case LAZY:
			return new LazyClassFileJar(jar);
		default:
			throw new RuntimeException("unknown mode: " + mode.name());
		}
	}

	/**
	 * Creates a {@link ClassFileCollection} from multiple JAR {@link File}s.
	 * Same as
	 * {@linkplain ClassFileCollection#getClassFileJarCollection(ClassFileJarMode, File...)
	 * getClassFileCollection(ClassFileJarMode.LAZY, jars)}.
	 * 
	 * @param jars
	 *            jars the {@code File}s to load
	 * @return the {@code ClassFileCollection} with lazy access to the classes
	 *         contained in the given JAR {code File}s
	 * @see ClassFileCollection#getClassFileJarCollection(ClassFileJarMode,
	 *      File...)
	 */
	public static ClassFileCollection getClassFileJarCollection(File... jars) {
		return getClassFileJarCollection(ClassFileJarMode.LAZY, jars);
	}

	/**
	 * Creates a {@link ClassFileCollection} from multiple JAR {@link File}s.
	 * The {@code File}s are searched in the order given.
	 * 
	 * @param mode
	 *            the mode of the {@code ClassFileCollection}s contained in this
	 *            collection
	 * @param jars
	 *            the {@code File}s to load
	 * @return the {@code ClassFileCollection} with access to the classes
	 *         contained in the given JAR {code File}s
	 */
	public static ClassFileCollection getClassFileJarCollection(
			ClassFileJarMode mode, File... jars) {
		return new ClassFileJarCollection(mode, jars);
	}

}
