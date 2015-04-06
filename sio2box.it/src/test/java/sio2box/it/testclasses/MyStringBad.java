package sio2box.it.testclasses;

public class MyStringBad {

    public char[] value = new char[10];


    public MyStringBad(String test) {
        value = test.toCharArray();
    }
}
