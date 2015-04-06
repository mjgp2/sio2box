package sio2box.it.testclasses;

import static sio2box.it.Sizes.FIRST_DIM;
import static sio2box.it.Sizes.SECOND_DIM;
import static sio2box.it.Sizes.THIRD_DIM;

import java.lang.reflect.Array;

import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;

@SuppressWarnings("unused")
@SiO2Class
public class ArrayTestClass {

    @SiO2Method
    public void booleanArray() {
        boolean[] array = new boolean[FIRST_DIM];
    }

    @SiO2Method
    public void charArray() {
        char[] array = new char[FIRST_DIM];
    }

    @SiO2Method
    public void floatArray() {
        float[] array = new float[FIRST_DIM];
    }

    @SiO2Method
    public void doubleArray() {
        double[] array = new double[FIRST_DIM];
    }

    @SiO2Method
    public void byteArray() {
        byte[] array = new byte[FIRST_DIM];
    }


    @SiO2Method
    public void shortArray() {
        short[] array = new short[FIRST_DIM];

    }

    @SiO2Method
    public void intArray() {
        int[] array = new int[FIRST_DIM];
    }

    @SiO2Method
    public void longArray() {
        long[] array = new long[FIRST_DIM];
    }

    @SiO2Method
    public void myStringArray() {
        MyString[] array = new MyString[FIRST_DIM];
    }


    @SiO2Method
    public void booleanMultiArray() {
        boolean[][][] array = new boolean[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void charMultiArray() {
        char[][][] array = new char[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void floatMultiArray() {
        float[][][] array = new float[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void doubleMultiArray() {
        double[][][] array = new double[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void byteMultiArray() {
        byte[][][] array = new byte[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void shortMultiArray() {
        short[][][] array = new short[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void intMultiArray() {
        int[][][] array = new int[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void longMultiArray() {
        long[][][] array = new long[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void myStringMutliArray() {
        MyString[][][] array = new MyString[FIRST_DIM][SECOND_DIM][THIRD_DIM];
    }

    @SiO2Method
    public void filledBooleanArray() {
        boolean[] array = new boolean[FIRST_DIM];
        for (int i = 0; i < FIRST_DIM; i++) {
            array[i] = i % 2 == 0;
        }
    }

    @SiO2Method
    public void filledCharArray() {
        char[] array = new char[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) i;
        }
    }

    @SiO2Method
    public void filledFloatArray() {
        float[] array = new float[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
    }

    @SiO2Method
    public void filledDoubleArray() {
        double[] array = new double[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
    }

    @SiO2Method
    public void filledByteArray() {
        byte[] array = new byte[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) i;
        }
    }


    @SiO2Method
    public void filledShortArray() {
        short[] array = new short[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = (short) i;
        }

    }

    @SiO2Method
    public void filledIntArray() {
        int[] array = new int[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
    }

    @SiO2Method
    public void filledLongArray() {
        long[] array = new long[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
    }

    @SiO2Method
    public void filledMyStringArray() {

        MyString[] array = new MyString[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            final String string = "hello";
            array[i] = new MyString(string);
        }
    }


    @SiO2Method
    public void myStringArrayNewInstance() {
        MyString[] array = (MyString[]) Array.newInstance(MyString.class, FIRST_DIM);
    }

    @SiO2Method
    public void myStringMultiArrayNewInstance() {
        int[] dims = {FIRST_DIM, SECOND_DIM};
        MyString[][] array = (MyString[][]) Array.newInstance(MyString.class, dims);
    }

    @SiO2Method
    public void arrayNewInstance() {
        int[] array = (int[]) Array.newInstance(int.class, FIRST_DIM);
    }

    @SiO2Method
    public void multiArrayNewInstance() {
        int[] dims = {FIRST_DIM, SECOND_DIM};
        int[][] array = (int[][]) Array.newInstance(int.class, dims);
    }

    @SiO2Method
    public void myStringArrayClone() {
        MyString[] array = (MyString[]) Array.newInstance(MyString.class, FIRST_DIM);
        array.clone();
    }

    @SiO2Method
    public void MyStringMultiArrayClone() {
        int[] dims = {FIRST_DIM, SECOND_DIM};
        MyString[][] array = (MyString[][]) Array.newInstance(MyString.class, dims);
        array.clone();
    }

    @SiO2Method
    public void arrayClone() {
        int[] array = (int[]) Array.newInstance(int.class, FIRST_DIM);
        array.clone();
    }

    @SiO2Method
    public void multiArrayClone() {
        int[] dims = {FIRST_DIM, SECOND_DIM};
        int[][] array = (int[][]) Array.newInstance(int.class, dims);
        array.clone();
    }

    @SiO2Method
    public void emptyArray() {
        int[] array = new int[0];
    }

}
