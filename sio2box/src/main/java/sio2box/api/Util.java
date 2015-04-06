package sio2box.api;


public class Util {

    public static void initialize() {
        // no-op
    }

    public static int padded(int value) {
        int padding = (8 - value) % 8;
        if (padding < 0) { // if negative then it will be 8 out, as modulo can
                           // be negative.
            padding += 8;
        }
        return value + padding;
    }

    public static int calculateBytesForArray(int size, int count) {
        return padded(12 + size * count);
    }

    public static int calculateBytesForMultiArray(int[] dims, int size) {
        int bytes = 0;
        for (int i = 0; i < dims.length; i++) {
            bytes = calculateBytesForArray((i == 0) ? size : 4 + bytes, dims[dims.length - 1 - i]);
        }
        return bytes;
    }

    public static int sizeOfClass(Class<?> clazz) {
        if (clazz.equals(boolean.class)) {
            return 1;
        } else if (clazz.equals(char.class)) {
            return 2;
        } else if (clazz.equals(float.class)) {
            return 4;
        } else if (clazz.equals(double.class)) {
            return 8;
        } else if (clazz.equals(byte.class)) {
            return 1;
        } else if (clazz.equals(short.class)) {
            return 2;
        } else if (clazz.equals(int.class)) {
            return 4;
        } else if (clazz.equals(long.class)) {
            return 8;
        } else { // Must be an object
            return 4; // Reference takes 4 bytes
        }
    }

    // Helper method to make sure memoryStore is initialized.
    public static MemoryStore initializeMemoryStore(MemoryStore memoryStore) {
        if (memoryStore == null) {
            memoryStore = new MemoryStore();
        }
        return memoryStore;
    }

}
