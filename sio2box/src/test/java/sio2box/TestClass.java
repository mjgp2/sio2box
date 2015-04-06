package sio2box;

import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;

@SiO2Class
public class TestClass {

    class A {

        public A value;
    }

    @SiO2Method
    public void loop() {
        while (true) {
        }
    }
}
