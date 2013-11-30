Classreader
===========

A library that parses class files that conform to the [Java Virtual Machine Specification][JVMS7]. It is licensed
under a 3-clause BSD license.

The resulting data structure can be used for introspection of class files:

- information about the class itself: super class, interfaces
- fields and methods
- parsed bytecode for methods
- attributes for class, fields and methods; all attributes from the spec are supported

Synopsis
========

```java
// single class files
ClassFile classFile = new ClassFile(new FileInputStream("path/to/Class.class")));
ConstantPool cp = classFile.getConstantPool();

for (ConstantPoolInfo cpi : cp) {
    // do something
}
System.err.println("class: " + cp.getUtf8Info(cp.getClassInfo(classFile.getThisClassIndex()).getNameIndex()).getValue());

System.err.println("fields:");
for (FieldInfo fi : classFile.getFields()) {
    System.err.println(" - " + cp.getUtf8Info(fi.getNameIndex()).getValue());
}
System.err.println("methods:");
for (MethodInfo mi : classFile.getMethods()) {
    System.err.println(" - " + cp.getUtf8Info(mi.getNameIndex()).getValue());
}

// JAR files
ClassFileCollection jar = ClassFileCollection.getClassFileCollection(
        new File("path/to/jar.jar"), ClassFileJarMode.EAGER); // LAZY is available too
// access known class
ClassFile classFile = jar.getClassFile("foo.bar.Foobar");

// iterate over classes (only works with EAGER)
for (String className : jar.getClassNames()) {
	ClassFile classFile = jar.getClassFile(className);
    // ...
}
```

The parsed structure of classreader closely resembles the [binary structure of a class file][JVMS7-CLASS].

Performance
===========

Classreader was written first with consistent code in mind and later tuned for performance. It is reasonably fast. Note
that some CPU intensive attributes (most notably the actual byte code) are parsed lazily. The most important aspect of a
class file are its symbols: The class file itself, its fields and methods. Lazy parsing of other attributes did not lead
to noticeable performance gains.

The `rt.jar` for Java 7 (1.7.0_45-b18) contains 18609 classes. Parsing this JAR with the `EagerClassFileJar` is used as
a benchmark throughout this section. The test runs on my i7-3517U @ 1.9 GHz (Turbo 3.0 GHz), Dual Core, HyperThreading.

Parsing the whole `rt.jar` takes between 5 and 7 seconds cold and between 1.5 and 6 seconds warm. Times vary wildly
because of garbage collection. Increasing the heap may greatly improve parse times. For example, setting `-Xms` and
`-Xmx` to 2g leads to a cold run in well under 3 seconds. Further runs vary between 1.6 and 7 seconds; however, manually
triggering garbage collection between every run consistently leads to times between 1.4 and 1.5 seconds. Of course the
time for garbage collection is still pretty significant.

It takes about 470 MB of heap memory. Iterating over all attributes and thus forcing to parse every deferred data
structure about doubles the required memory to 1 GB and time (about 8.8 seconds cold with 2 GB heap).

All in all you get "up to" 11 classes per millisecond and generally about 3 classes per millisecond. I don't think there
will be much improvement without rewriting a whole lot (if possible at all).

Multithreading does not bring any gains for the first run. Subsequent runs are considerably faster when triggering
garbage collection manually and get consistently below 1 second for two threads. (Since my CPU has only two physical
cores with four hyperthreads, raising to four threads doesn't do a thing. I'd be interested to run this on a quad core.)
Of course this doesn't really represent a realistic workflow...

 [JVMS7]: http://docs.oracle.com/javase/specs/jvms/se7/html/index.html
 [JVMS7-CLASS]: http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
