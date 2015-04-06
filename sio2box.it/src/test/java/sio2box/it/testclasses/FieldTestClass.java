package sio2box.it.testclasses;

import sio2box.annotations.SiO2Class;
import sio2box.annotations.SiO2Method;

@SuppressWarnings("unused")
@SiO2Class
public class FieldTestClass {

    private class EmbeddedClass {

        private int hello = 5;
        private double hello2 = 2.2222222221d;
        private long hello3 = 1111111111111111111l;

        EmbeddedClass() {

        }
    }

    private class EmbeddedClassWithDefaultConstructor {

        private int hello = 5;
        private double hello2 = 2.2222222221d;
        private long hello3 = 1111111111111111111l;
    }

    @SiO2Method
    public void embeddedClass() {
        new EmbeddedClass();
    }

    @SiO2Method
    public void embeddedClassWithDefaultConstructor() {
        new EmbeddedClassWithDefaultConstructor();
    }

    @SiO2Method
    public void classWithAField() {
        new ClassWithAField();
    }

    @SiO2Method
    public void subClassWithoutFields() {
        new SubClassWithoutFields();
    }

    @SiO2Method
    public void extendsAbstractClassWithFields() {
        new ExtendsAbstractClassWithFields();
    }

    @SiO2Method
    public void classWithStaticFields() {
        new ClassWithStaticFields();
    }
}
