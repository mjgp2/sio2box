package sio2box.it;

import static org.junit.Assert.assertEquals;
import static sio2box.it.Sizes.ARRAY_OVERHEAD;
import static sio2box.it.Sizes.BOOLEAN;
import static sio2box.it.Sizes.BYTE;
import static sio2box.it.Sizes.CHAR;
import static sio2box.it.Sizes.DOUBLE;
import static sio2box.it.Sizes.FIRST_DIM;
import static sio2box.it.Sizes.FLOAT;
import static sio2box.it.Sizes.INT;
import static sio2box.it.Sizes.LONG;
import static sio2box.it.Sizes.OBJECT_OVERHEAD;
import static sio2box.it.Sizes.OBJECT_REF;
import static sio2box.it.Sizes.SECOND_DIM;
import static sio2box.it.Sizes.SHORT;
import static sio2box.it.Sizes.THIRD_DIM;
import static sio2box.it.Sizes.padded;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import sio2box.api.ThreadTrackingContext;
import sio2box.it.testclasses.ArrayTestClass;

public class ArrayIT {

    @Test
    public void primtiveArraysTest() {
        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.booleanArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * BOOLEAN), ThreadTrackingContext.getBytes());

        arrayTestClass.charArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * CHAR), ThreadTrackingContext.getBytes());

        arrayTestClass.floatArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * FLOAT), ThreadTrackingContext.getBytes());

        arrayTestClass.doubleArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * DOUBLE), ThreadTrackingContext.getBytes());

        arrayTestClass.byteArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * BYTE), ThreadTrackingContext.getBytes());

        arrayTestClass.shortArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * SHORT), ThreadTrackingContext.getBytes());

        arrayTestClass.intArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * INT), ThreadTrackingContext.getBytes());

        arrayTestClass.longArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void filledPrimtiveArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass(); // INT in each case is used by the for
                                                              // loop.

        arrayTestClass.filledBooleanArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * BOOLEAN), ThreadTrackingContext.getBytes());

        arrayTestClass.filledCharArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * CHAR), ThreadTrackingContext.getBytes());

        arrayTestClass.filledFloatArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * FLOAT), ThreadTrackingContext.getBytes());

        arrayTestClass.filledDoubleArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * DOUBLE), ThreadTrackingContext.getBytes());

        arrayTestClass.filledByteArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * BYTE), ThreadTrackingContext.getBytes());

        arrayTestClass.filledShortArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * SHORT), ThreadTrackingContext.getBytes());

        arrayTestClass.filledIntArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * INT), ThreadTrackingContext.getBytes());

        arrayTestClass.filledLongArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void booleanMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.booleanMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * BOOLEAN))))),
                ThreadTrackingContext.getBytes());
    }

    @Test
    public void charMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.charMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * CHAR))))),
                ThreadTrackingContext.getBytes());

    }

    @Test
    public void floatMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.floatMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * FLOAT))))),
                ThreadTrackingContext.getBytes());

    }

    @Test
    public void doubleMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.doubleMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * DOUBLE))))),
                ThreadTrackingContext.getBytes());

    }

    @Test
    public void byteMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.byteMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * BYTE))))),
                ThreadTrackingContext.getBytes());

    }

    @Test
    public void shortMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.shortMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * SHORT))))),
                ThreadTrackingContext.getBytes());
    }

    @Test
    public void intMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.intMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * INT))))),
                ThreadTrackingContext.getBytes());
    }

    @Test
    public void longMultiArraysTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.longMultiArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * LONG))))),
                ThreadTrackingContext.getBytes());
    }


    @Test
    public void myStringArrayTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.myStringArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void filledMyStringArrayTest() {
        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.filledMyStringArray();
        final long bytes = ThreadTrackingContext.getBytes();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF) + FIRST_DIM * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR)), bytes); // Length
                                                                                                                                                                       // of
                                                                                                                                                                       // MyString
                                                                                                                                                                       // is
                                                                                                                                                                       // 5,
                                                                                                                                                                       // extra
                                                                                                                                                                       // INT
                                                                                                                                                                       // is
                                                                                                                                                                       // for
                                                                                                                                                                       // the
                                                                                                                                                                       // for
                                                                                                                                                                       // loop.
    }

    @Test
    public void stringMutliArrayTest() {

        // load JDK class
        new ArrayBlockingQueue<Object>(10);

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.myStringMutliArray();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + THIRD_DIM * OBJECT_REF))))),
                ThreadTrackingContext.getBytes());
    }

    @Test
    public void emptyArrayTest() {
        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.emptyArray();
        assertEquals(padded(ARRAY_OVERHEAD), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringArrayNewInstanceTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.myStringArrayNewInstance();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringMultiArrayNewInstanceTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.myStringMultiArrayNewInstance();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * OBJECT_REF))) + padded(ARRAY_OVERHEAD + 2 * INT), ThreadTrackingContext.getBytes());
    }



    @Test
    public void arrayNewInstanceTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.arrayNewInstance();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * INT), ThreadTrackingContext.getBytes());
    }

    @Test
    public void multiArrayNewInstanceTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.multiArrayNewInstance();
        assertEquals(padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * INT))) + padded(ARRAY_OVERHEAD + 2 * INT), ThreadTrackingContext.getBytes());
    }

    @Test
    public void myStringArrayCloneTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.myStringArrayClone();
        assertEquals(2 * padded(ARRAY_OVERHEAD + FIRST_DIM * OBJECT_REF), ThreadTrackingContext.getBytes());
    }


    @Test
    public void myStringMultiArrayCloneTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.MyStringMultiArrayClone();
        assertEquals(2 * padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * OBJECT_REF))) + padded(ARRAY_OVERHEAD + 2 * INT), ThreadTrackingContext.getBytes());
    }

    @Test
    public void arrayCloneTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.arrayClone();
        assertEquals(2 * padded(ARRAY_OVERHEAD + FIRST_DIM * INT), ThreadTrackingContext.getBytes());
    }

    @Test
    public void multiArrayCloneTest() {

        // load our test class
        ArrayTestClass arrayTestClass = new ArrayTestClass();

        arrayTestClass.multiArrayClone();
        assertEquals(2 * padded(ARRAY_OVERHEAD + FIRST_DIM * (OBJECT_REF + padded(ARRAY_OVERHEAD + SECOND_DIM * OBJECT_REF))) + padded(ARRAY_OVERHEAD + 2 * INT), ThreadTrackingContext.getBytes());
    }
}
