package sio2box.it;

import static org.junit.Assert.assertEquals;
import static sio2box.it.Sizes.BOOLEAN;
import static sio2box.it.Sizes.BYTE;
import static sio2box.it.Sizes.CHAR;
import static sio2box.it.Sizes.DOUBLE;
import static sio2box.it.Sizes.FLOAT;
import static sio2box.it.Sizes.INT;
import static sio2box.it.Sizes.LONG;
import static sio2box.it.Sizes.OBJECT_OVERHEAD;
import static sio2box.it.Sizes.padded;

import org.junit.Test;

import sio2box.api.ThreadTrackingContext;
import sio2box.it.testclasses.FieldTestClass;

public class FieldIT {

    @Test
    public void classWithAFieldTest() {

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * public class ClassWithAField { private int hello = 5; private double hello2 =
         * 2.2222222221d; private long hello3 = 1111111111111111111l; }
         */
        fieldTestClass.classWithAField();
        assertEquals(padded(OBJECT_OVERHEAD + INT + DOUBLE + LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void embeddedClassTest() {

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * private class EmbeddedClass { private int hello = 5; private double hello2 =
         * 2.2222222221d; private long hello3 = 1111111111111111111l;
         * 
         * EmbeddedClass() {
         * 
         * } }
         */
        fieldTestClass.embeddedClass();
        assertEquals(padded(OBJECT_OVERHEAD + INT + DOUBLE + LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void embeddedClassWithDefaultConstructorTest() {

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * private class EmbeddedClass { private int hello = 5; private double hello2 =
         * 2.2222222221d; private long hello3 = 1111111111111111111l; }
         */
        fieldTestClass.embeddedClassWithDefaultConstructor();
        assertEquals(padded(OBJECT_OVERHEAD + INT + DOUBLE + LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void subClassWithoutfieldsTest() {

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * public class SubClassWithoutFields extends SuperClassWithFields { }
         * 
         * and SuperClassWithFields is defined as:
         * 
         * public class SuperClassWithFields { public boolean booleanField; public char charField;
         * public float floatField; public double doubleField; public byte byteField; public short
         * shortField; public int intField; public long longField; }
         */
        fieldTestClass.subClassWithoutFields();
        assertEquals(padded(OBJECT_OVERHEAD + BOOLEAN + CHAR + FLOAT + DOUBLE + BYTE + CHAR + INT + LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void extendsAbstractClassWithFieldsTest() {

        new FieldTestClass();

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * public class ExtendsAbstractClassWithoutFields extends AbstractClassWithFields { }
         * 
         * and AbstractClassWithFields is defined as:
         * 
         * public abstract class AbstractClassWithFields { public boolean booleanField; public char
         * charField; public float floatField; public double doubleField; public byte byteField;
         * public short shortField; public int intField; public long longField; }
         */
        fieldTestClass.extendsAbstractClassWithFields();
        assertEquals(padded(OBJECT_OVERHEAD + BOOLEAN + CHAR + FLOAT + DOUBLE + BYTE + CHAR + INT + LONG), ThreadTrackingContext.getBytes());
    }

    @Test
    public void classWithStaticFieldsTest() {

        // load our test class
        FieldTestClass fieldTestClass = new FieldTestClass();
        /*
         * This instantiates a:
         * 
         * public class ExtendsAbstractClassWithoutFields extends AbstractClassWithFields { }
         * 
         * and AbstractClassWithFields is defined as:
         * 
         * public abstract class AbstractClassWithFields { public boolean booleanField; public char
         * charField; public float floatField; public double doubleField; public byte byteField;
         * public short shortField; public int intField; public long longField; }
         */
        fieldTestClass.classWithStaticFields();
        assertEquals(padded(OBJECT_OVERHEAD), ThreadTrackingContext.getBytes());
    }

}
