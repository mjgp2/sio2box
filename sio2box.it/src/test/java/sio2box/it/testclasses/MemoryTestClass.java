package sio2box.it.testclasses;

import static sio2box.it.Sizes.FIRST_DIM;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;

import lombok.SneakyThrows;
import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;
import sio2box.api.MemoryStore;

@SuppressWarnings("unused")
@SiO2Class
public class MemoryTestClass {


    @SneakyThrows
    @SiO2Method
    public void stringConstructorNewInstance(Constructor<String> stringCon, Object[] args) {
        String hello = stringCon.newInstance(args);
    }

    @SiO2Method
    public void firstTestTwo(MemoryStore memoryStoreOne, MemoryStore memoryStoreTwo) {
        MyString[] array = new MyString[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyString("hello");
        }
    }

    @SiO2Method
    public void secondTestTwo(MemoryStore memoryStoreOne, MemoryStore memoryStoreTwo) {
        Object[] array = new Object[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Object();
        }
    }

    @SiO2Method
    public void firstTest(MemoryStore memoryStore) {
        MyString[] array = new MyString[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyString("hello");
        }
    }

    @SiO2Method
    public void secondTest(MemoryStore memoryStore) {
        Object[] array = new Object[FIRST_DIM];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Object();
        }
    }

    @SiO2Method
    public void exceedMemory(MemoryStore m) {
        int[] moreThan10Bytes = new int[1];
    }

    @SiO2Method
    public void primitiveBoolean() {
        boolean aBoolean = true;
    }

    @SiO2Method
    public void primitiveChar() {
        char aChar = 'A';
    }

    @SiO2Method
    public void primitiveFloat() {
        float aFloat = 5.1f;
    }

    @SiO2Method
    public void primitiveDouble() {
        double aDouble = 5.11111111111d;
    }

    @SiO2Method
    public void primitiveByte() {
        byte aByte = 1;
    }


    @SiO2Method
    public void primitiveShort() {
        short aShort = 128;
    }

    @SiO2Method
    public void primitiveInt() {
        int anInt = 421312312;
    }

    @SiO2Method
    public void primitiveLong() {
        long aLong = 4111111111111111l;
    }

    @SiO2Method
    public void string() {
        String hello = new String("hellohellohellohello");
    }

    @SneakyThrows
    @SiO2Method
    public void stringNewInstance() {
        String hello = String.class.newInstance();
    }

    @SiO2Method
    public void arrayListClone(ArrayList<Object> list) {
        list.clone();
    }
    
    @SiO2Method
    public void dateClone(Date d) {
        d.clone();
    }

    @SiO2Method
    public void arrayList(int capacity) {
        ArrayList<Object> anArrayList = new ArrayList<Object>(capacity);
    }


    @SiO2Method
    public void myString(String input) {
        MyString hello = new MyString(input);
    }

    @SiO2Method
    public void myStringPassedThrough(MyString input) {
        MyString hello = new MyString(input);
    }

    @SiO2Method
    public void myStringBad(String input) {
        MyStringBad hello = new MyStringBad(input);
    }

    @SiO2Method
    public void newInteger() {
        Integer anInteger = new Integer(200);
    }
}
