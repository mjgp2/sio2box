package sio2box.it.testclasses;


public class MyString {

    public char[] value;


    public MyString(String input) {
        value = input.toCharArray();
    }

    public MyString(MyString input) {
        value = input.value;
    }
}
