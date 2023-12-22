# java-class-loading-playground

📚 Learning and exploring how class loading works in the JVM.

**NOTE**: This project was developed on macOS. It is designed for my own personal use.


## Overview

How does class loading work? What is the system class loader? What is the effect of two classes, with the same fully
qualified name, existing on the classpath? What does it even mean to load a class? Does a class get loaded "into a
classloading space" or does a class get loaded into a shared space for the whole process? Does JPMS let us have strong
encapsulation of this space?

This codebase is me exploring class loading by way of runnable examples.


## Instructions

Follow these instructions to build and run the example program.

1. Pre-requisite: Java 21
2. Build the program `.jar` file:
   * ```shell
     ./gradlew jar
     ```
3. Run the program
   * ```shell
     java -jar build/libs/java-class-loading-playground.jar
     ```
   * Study the output and study the code that produced it. You'll see how class loaders can be used to load the same
     class definition (`.class` but) twice and it being treated as two different classes at runtime. Interesting!
   * The output looks like this:
     ```text
     MessageHolder{staticMessage='Hello', classloader='jdk.internal.loader.ClassLoaders$AppClassLoader@63c12fb0'}
     MessageHolder{staticMessage='Hello', classloader='java.net.URLClassLoader@44e81672'}
     
     Logically equal?: false
     Reference-equal?: false
     
     MessageHolder{staticMessage='Goodbye', classloader='jdk.internal.loader.ClassLoaders$AppClassLoader@63c12fb0'}
     MessageHolder{staticMessage='Hello', classloader='java.net.URLClassLoader@44e81672'}
     
     instanceFromAppLoader: MessageHolder{instanceMessage='I'm from the app/system class loader.'}
     instanceFromCustomLoader: MessageHolder{instanceMessage='I'm from a custom class loader.'}
     instanceFromAppLoader type: class dgroomes.MessageHolder
     instanceFromCustomLoader type: class dgroomes.MessageHolder
     instanceFromAppLoader is an instance of MessageHolder
     instanceFromCustomLoader is NOT an instance of MessageHolder
     ```


## Wish List

General clean-ups, TODOs and things I wish to implement for this project:

* [x] DONE Illustrate the effect of a custom user class loader. Can I load the same class binary twice, with different
  class loaders, and then print the classes classloader/hashCode to show that are different classes at runtime? What happens
  to their static fields? This is like some trippy parallel universe stuff. Also, I don't get the difference or the point
  of the difference between the bootstrap class loader and the system class loader.
  * SKIP Consider dropping `.jar` files and just outputting and coding to `.class` files. The reader can make view a `.class`
    file in a file explorer whereas a `.jar` file needs to be unzipped. Remember, this is a demo.


## Reference

* [Java Language Specification: *12.2. Loading of Classes and Interfaces*](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html#jls-12.2)
* [Java Virtual Machine Specification: *Chapter 5. Loading, Linking, and Initializing*](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-5.html)
  * > After creation, a class or interface is determined not by its name alone, but by a pair: its binary name (§4.2.1) and its defining loader. Each such class or interface belongs to a single run-time package. The run-time package of a class or interface is determined by the package name and the defining loader of the class or interface. 
