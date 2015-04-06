package sio2box.it.testclasses;

import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;

@SuppressWarnings("unused")
@SiO2Class
public class IgnoredClass {

    @SiO2Method
    public static void ignoredMethod() {
        Integer anotherInt = new Integer(500);
    }
}
