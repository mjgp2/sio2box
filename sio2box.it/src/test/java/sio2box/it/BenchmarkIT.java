package sio2box.it;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static sio2box.it.Sizes.ARRAY_OVERHEAD;
import static sio2box.it.Sizes.CHAR;
import static sio2box.it.Sizes.INT;
import static sio2box.it.Sizes.OBJECT_OVERHEAD;
import static sio2box.it.Sizes.OBJECT_REF;
import static sio2box.it.Sizes.padded;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import sio2box.agent.ThreadTrackingData;
import sio2box.api.MemoryExceededException;
import sio2box.api.MemoryStore;
import sio2box.api.ThreadTrackingContext;
import sio2box.it.testclasses.BenchmarkTestClass;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.IResultsConsumer;
import com.carrotsearch.junitbenchmarks.XMLConsumer;

public class BenchmarkIT {

    private static IResultsConsumer consumers = null;
    static {
        try {
            consumers = new XMLConsumer(new File("BenchmarkTest.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule(consumers);

    @After
    public void after() {
        ThreadTrackingData data = ThreadTrackingContext.getThreadContext();
        if (data.getNumDeep() > 0) {
            System.err.println("**** NOT EXITED CLEANLY");
        }
        data.halt();
    }

    @Test(expected = MemoryExceededException.class, timeout = 10000)
    public void loopTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        MemoryStore memoryStore = new MemoryStore(50000000);
        benchmarkTestClass.loop(memoryStore);
        fail();
    }

    @Test
    public void sumEvenFibonacciTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.sumEvenFibonacci();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void multiplesOf3And5Test() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.multiplesOf3And5();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void createObjectsEmptyArrayNoTrackingTest() {

        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();


        benchmarkTestClass.createObjectsEmptyArrayNoTracking();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void createObjectsEmptyArrayTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createObjectsEmptyArray();
        assertEquals(padded(ARRAY_OVERHEAD + BenchmarkTestClass.ARRAY_LENGTH * OBJECT_REF), ThreadTrackingContext.getBytes());

    }

    @Test
    public void createObjectsNoTrackingTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createObjectsNoTracking();
        assertEquals(0, ThreadTrackingContext.getBytes());


    }

    @Test
    public void createObjectsTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createObjects();
        assertEquals(padded(ARRAY_OVERHEAD + BenchmarkTestClass.ARRAY_LENGTH * OBJECT_REF) + BenchmarkTestClass.ARRAY_LENGTH * padded(OBJECT_OVERHEAD), ThreadTrackingContext.getBytes());
    }

    @Test
    public void createStringsNoTrackingTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createStringsNoTracking();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void createStringsTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createStrings();
        assertEquals(padded(ARRAY_OVERHEAD + BenchmarkTestClass.ARRAY_LENGTH * OBJECT_REF) + BenchmarkTestClass.ARRAY_LENGTH
                * (padded(OBJECT_OVERHEAD + OBJECT_REF) + padded(ARRAY_OVERHEAD + 5 * CHAR)), ThreadTrackingContext.getBytes());
    }



    @Test
    public void createIntsNoTrackingTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();

        benchmarkTestClass.createIntsNoTracking();
        assertEquals(0, ThreadTrackingContext.getBytes());
    }

    @Test
    public void createIntsTest() {
        // load our test class
        BenchmarkTestClass benchmarkTestClass = new BenchmarkTestClass();
        benchmarkTestClass.createInts();
        assertEquals(padded(ARRAY_OVERHEAD + BenchmarkTestClass.ARRAY_LENGTH * INT), ThreadTrackingContext.getBytes());
    }
}
