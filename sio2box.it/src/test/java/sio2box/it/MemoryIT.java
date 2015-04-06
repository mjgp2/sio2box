package sio2box.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static sio2box.it.Sizes.ARRAY_OVERHEAD;
import static sio2box.it.Sizes.CHAR;
import static sio2box.it.Sizes.FIRST_DIM;
import static sio2box.it.Sizes.INT;
import static sio2box.it.Sizes.LONG;
import static sio2box.it.Sizes.OBJECT_OVERHEAD;
import static sio2box.it.Sizes.OBJECT_REF;
import static sio2box.it.Sizes.padded;

import java.util.ArrayList;
import java.util.Date;

import lombok.SneakyThrows;

import org.junit.Ignore;
import org.junit.Test;

import sio2box.api.MemoryExceededException;
import sio2box.api.MemoryStore;
import sio2box.api.ThreadTrackingContext;
import sio2box.it.testclasses.MemoryTestClass;
import sio2box.it.testclasses.MyString;


public class MemoryIT {


    @Test
    @SneakyThrows
    public void stringConstructorNewInstanceTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.stringConstructorNewInstance(String.class.getConstructor(String.class), new Object[] {"hellohellohellohello"});

        // hashcode & reference to the char array from the copied string
        assertEquals(padded(OBJECT_OVERHEAD + INT + OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void twoResumeMemoryTest() {
        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();
        MemoryStore memStoreOne = new MemoryStore();
        memStoreOne.setUsedMemory(100);
        MemoryStore memStoreTwo = new MemoryStore();
        memStoreTwo.setUsedMemory(1000);
        memoryTestClass.firstTestTwo(memStoreOne, memStoreTwo); // Creates filled array of MyString
                                                                // with String "hello" and length
                                                                // FIRST_DIM.
        long size1 = padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR));
        assertEquals(100 + size1, memStoreOne.getUsedMemory());
        assertEquals(1000, memStoreTwo.getUsedMemory());

        memoryTestClass.secondTestTwo(memStoreOne, memStoreTwo);// Creates filled array of Object
                                                                // and length FIRST_DIM.
        assertEquals(100 + size1 + padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * padded(OBJECT_OVERHEAD), memStoreOne.getUsedMemory());
        assertEquals(1000, memStoreTwo.getUsedMemory());
    }

    @Test
    public void twoResumeMemoryWithNullTest() {
        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();
        MemoryStore memStoreOne = null;
        MemoryStore memStoreTwo = new MemoryStore();
        memoryTestClass.firstTestTwo(memStoreOne, memStoreTwo); // Creates filled array of MyString
                                                                // with String "hello" and length
                                                                // FIRST_DIM.
        long size1 = padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR));
        assertEquals(size1, ThreadTrackingContext.getBytes());
        assertEquals(0, memStoreTwo.getUsedMemory());

        memoryTestClass.secondTestTwo(memStoreOne, memStoreTwo);// Creates filled array of Object
                                                                // and length FIRST_DIM.
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * padded(OBJECT_OVERHEAD), ThreadTrackingContext.getBytes());
        assertEquals(0, memStoreTwo.getUsedMemory());
    }

    @Test
    public void resumeMemoryTest() {
        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();
        MemoryStore memStore = new MemoryStore();

        memoryTestClass.firstTest(memStore); // Creates filled array of MyString with String "hello"
                                             // and length FIRST_DIM.
        long size1 = padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR));
        assertEquals(size1, memStore.getUsedMemory());

        memoryTestClass.secondTest(memStore);// Creates filled array of Object and length FIRST_DIM.
        assertEquals(size1 + padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * padded(OBJECT_OVERHEAD), memStore.getUsedMemory());
    }

    @Test
    public void resumeMemoryWithNullTest() {
        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.firstTest(null); // Creates filled array of MyString with String "hello" and
                                         // length FIRST_DIM.
        long size1 = padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR));
        assertEquals(size1, ThreadTrackingContext.getBytes());

        memoryTestClass.secondTest(null);// Creates filled array of Object and length FIRST_DIM.
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * padded(OBJECT_OVERHEAD), ThreadTrackingContext.getBytes());
    }


    @Test(expected = MemoryExceededException.class)
    public void exceedMemoryTest() {
        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();
        MemoryStore memoryStore = new MemoryStore(10);

        memoryTestClass.exceedMemory(memoryStore);
        fail();
    }

    @Test
    public void localPrimtivesTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.primitiveBoolean();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveChar();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveFloat();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveDouble();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveByte();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveChar();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveInt();
        assertEquals(0, ThreadTrackingContext.getBytes());

        memoryTestClass.primitiveLong();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void testNewInteger() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.newInteger();
        assertEquals(padded(OBJECT_OVERHEAD + INT), ThreadTrackingContext.getBytes());
    }

    @Test
    public void stringTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.string();

        // hashcode & reference to the char array from the copied string
        assertEquals(padded(OBJECT_OVERHEAD + INT + OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void stringNewInstanceTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.stringNewInstance();

        // uses a constant
        assertEquals(0, ThreadTrackingContext.getBytes());
    }


    @Test
    public void arrayListTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.arrayList(10);
        assertEquals(padded(OBJECT_OVERHEAD + INT * 2 + OBJECT_REF) + padded(ARRAY_OVERHEAD + 10 * OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.myString("hellohe");// String length = 7
        assertEquals(padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 7 * CHAR), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringPassedThroughTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.myStringPassedThrough(new MyString("hellohe"));// String length = 7
        assertEquals(padded(OBJECT_OVERHEAD + OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringBadTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.myStringBad("hellohe");// String length = 7
        assertEquals(padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 7 * CHAR) + padded(ARRAY_OVERHEAD + 10 * CHAR), ThreadTrackingContext.getBytes());
    }

    @Test
    @Ignore // TODO: ArrayList.clone actually duplicates the array
    public void cloneTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.arrayListClone(new ArrayList<Object>(10));

        // when running through maven the JDK seems to just ignore the bytes for copying the array
        assertEquals(padded(OBJECT_OVERHEAD + OBJECT_REF + INT + INT), ThreadTrackingContext.getBytes());
    }
    
    @Test
    public void dateCloneTest() {

        // load our test class
        MemoryTestClass memoryTestClass = new MemoryTestClass();

        memoryTestClass.dateClone(new Date());

        // when running through maven the JDK seems to just ignore the bytes for copying the array
        assertEquals(padded(OBJECT_OVERHEAD + OBJECT_REF + LONG), ThreadTrackingContext.getBytes());
    }

}
