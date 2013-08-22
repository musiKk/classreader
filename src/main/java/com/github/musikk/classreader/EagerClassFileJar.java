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
package com.github.musikk.classreader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EagerClassFileJar extends ClassFileCollection {

	private final File jarFile;

	private final Map<String, ClassFile> zipContent;

	public EagerClassFileJar(File jarFile) {
		this.jarFile = jarFile;
		this.zipContent = new HashMap<String, ClassFile>();
		parseContents();
	}

	private void parseContents() {

		try {
			ZipFile zipFile = new ZipFile(jarFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {

				ZipEntry entry = entries.nextElement();
				String entryName = entry.getName();
				if (!isClassEntry(entryName)) {
					continue;
				}

				InputStream is = zipFile.getInputStream(entry);
				try {
					ClassFile cf = new ClassFile(is);
					zipContent.put(entryName, cf);
					is.close();
				} catch (RuntimeException e) {
					is.close();
					throw new RuntimeException("error loading class '"
							+ entryName + "'", e);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private static boolean isClassEntry(String entryName) {
		if (entryName.endsWith(".class")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ClassFile getClassFile(String className) {
		return this.zipContent.get(className);
	}

	@Override
	public Collection<String> getClassNames() {
		return Collections.unmodifiableCollection(this.zipContent.keySet());
	}
}
