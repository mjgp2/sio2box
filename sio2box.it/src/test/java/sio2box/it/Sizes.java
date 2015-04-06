package sio2box.it;

public class Sizes {

    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int FLOAT = 4;
    public static final int DOUBLE = 8;
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INT = 4;
    public static final int LONG = 8;

    public static final int OBJECT_OVERHEAD = 8;
    public static final int OBJECT_REF = 4;
    public static final int ARRAY_OVERHEAD = 12;

    public static final int FIRST_DIM = 500;
    public static final int SECOND_DIM = 200;
    public static final int THIRD_DIM = 100;

    public static int padded(int value) {
        int padding = (8 - value) % 8;
        if (padding < 0) { // if negative then it will be 8 out, as modulo can be negative.
            padding += 8;
        }
        return value + padding;
    }
}
