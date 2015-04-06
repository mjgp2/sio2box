package sio2box;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

import sio2box.agent.SiO2ClassFileTransformer;

public class Test {

    @org.junit.Test
    public void test() throws Throwable {
        try {
            byte[] bytes = toBytes(TestClass.class);
            bytes = new SiO2ClassFileTransformer().transform(TestClass.class.getName(), bytes);
            CheckClassAdapter.verify(new ClassReader(bytes), false, new PrintWriter(System.err));
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    private byte[] toBytes(Class<?> clazz) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        String name = clazz.getName().replace('.', '/') + ".class";
        try (InputStream iStream = clazz.getClassLoader().getResourceAsStream(name)) {
            while ((nRead = iStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }


}
