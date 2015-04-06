sio2box
=======

A sandbox that tracks JVM memory allocation, and will throw an exception when a limit is reached.

## Tracks

* Array.newInstance
* type[].clone
* Object instantiation
* new type[] - ANEWARRAY, NEWARRAY, MULTIANEWARRAY
* Object.clone
* ArrayList.clone (TODO)

Doesn't track any local primitive allocations. 

## Compiling

```
cd sio2box
mvn install
```

## Usage

```
java
-javaagent:sio2box.jar
-Xbootclasspath/a:sio2box.jar
...
```

Pass through a `MemoryStore` object as the first argument (this is a convention).

```
MemoryStore memoryStore = new MemoryStore(maxMemory);
myMethod(memoryStore, arg0, arg1);
```

Annotate the class and method:

```
@SiO2Class
public class MyClass {

  @SiO2Method
  public void myMethod(MemoryStore m, String arg0, Object arg1) {
      ...
  }
}
```

If more memory is allocated than you specify, a `MemoryExceededException` exception is thrown.