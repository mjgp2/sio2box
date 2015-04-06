package sio2box.it.testclasses;

import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;
import sio2box.api.MemoryStore;

@SiO2Class
public class BenchmarkTestClass {

    public final static int ARRAY_LENGTH = 1000000;
    static double goldenRatio = (1 + Math.sqrt(5)) / 2;

    class A {

        public A value;
    }

    @SiO2Method
    public void loop(MemoryStore m) {
        A objOne = new A();
        while (true) {
            A objTwo = new A();
            objTwo.value = objOne;
            objOne = objTwo;
        }
    }

    public int nthFibanacii(int n) {
        return (int) ((Math.pow(goldenRatio, n) - Math.pow(-goldenRatio, -n)) / Math.sqrt(5));
    }

    @SiO2Method
    public int sumEvenFibonacci() {
        int sum = 0;
        int ans = 0;
        int n = 3;
        do {
            sum += ans;
            ans = nthFibanacii(n);
            n += 3;
        } while (ans < 4000000);

        return sum;
    }

    @SiO2Method
    public int multiplesOf3And5() {
        int sum = 0;
        for (int i = 1; i < 1000; i++) {
            if ((i % 3) == 0 || (i % 5) == 0) {
                sum += i;
            }
        }
        return sum;
    }

    public void myStringsArray() {
        MyString[] array = new MyString[ARRAY_LENGTH];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyString("hello");
        }
    }

    @SiO2Method
    public void createStrings() {
        myStringsArray();
    }

    public void createStringsNoTracking() {
        myStringsArray();
    }

    public void emptyObjectArray() {
        @SuppressWarnings("unused")
        Object[] array = new Object[ARRAY_LENGTH];
    }


    @SiO2Method
    public void createObjectsEmptyArray() {
        emptyObjectArray();
    }

    public void createObjectsEmptyArrayNoTracking() {
        emptyObjectArray();
    }

    public void objectArray() {
        Object[] array = new Object[ARRAY_LENGTH];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Object(); // Testing!
        }
    }

    @SiO2Method
    public void createObjects() {
        objectArray();
    }

    public void createObjectsNoTracking() {
        objectArray();
    }



    public void intArray() {
        int[] array = new int[ARRAY_LENGTH];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
    }

    public void createIntsNoTracking() {
        intArray();
    }

    @SiO2Method
    public void createInts() {
        intArray();
    }

}
