package sio2box.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

@FieldDefaults(level=AccessLevel.PRIVATE)
public class SiO2ClassFileTransformer implements ClassFileTransformer, Opcodes {

    static final String CLASSLOADER = ClassLoader.getSystemClassLoader().getClass().getName().replace('.', '/');
    static final String CLASS = Class.class.getName().replace('.', '/');

    @Setter static List<String> notWeavedPrefixes = Arrays.asList(new String[] {
            "java/lang/Shutdown",
            "java/lang/Thread", // ignore all things thread
            "java/lang/reflect/",
            "java/lang/ref/",
            "sun/reflect/",
            "sio2box/agent/",
            "sio2box/api/"
    });

    @Setter static List<String> methodsIgnoredByTracking = Arrays.asList(CLASSLOADER, CLASS);

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        return transform(className, bytes);
    }

    public byte[] transform(String className, byte[] bytes) {
        
        for (String ignore : notWeavedPrefixes) {
            if (className.startsWith(ignore)) {
                return bytes;
            }
        }

        // recompute the stack map frames that give verification information to the JVM
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassReader classReader = new ClassReader(bytes);

        ClassVisitor classVisitor = new SiO2ClassVisitor(classWriter, className, methodsIgnoredByTracking);
        
        // don't visit the frames because they will be recomputed; see ClassWriter.COMPUTE_FRAMES above
        classReader.accept(classVisitor, ClassReader.SKIP_FRAMES);
        
        return classWriter.toByteArray();
    }
}
