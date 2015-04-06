package sio2box.agent;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import sio2box.annotations.SiO2Class;
import sio2box.api.ThreadTrackingContext;

@FieldDefaults(level=AccessLevel.PRIVATE)
public class SiO2ClassVisitor extends ClassVisitor implements Opcodes {

    static final String CLASS_ANNOTATION = Type.getDescriptor(SiO2Class.class);

    @Getter boolean enabledClass = false;

    String className;
    int fieldByteCount;
    List<String> methodsIgnoredByTracking;

    public SiO2ClassVisitor(ClassWriter classWriter, String className, List<String> methodsIgnoredByTracking) {
        super(Opcodes.ASM5, classWriter);
        this.className = className;
        this.methodsIgnoredByTracking = methodsIgnoredByTracking;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        
        // looking for the @SiO2Class annotation
        if (desc.equals(CLASS_ANNOTATION)) {
            enabledClass = true;
        }
        
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        
        // if this is is an instance field, add its size to the tally
        // don't count synthetic fields
        if ( !staticAccess(access) && !synthetic(access) ) {
            fieldByteCount += signatureToBytes(desc);
        }
        
        return super.visitField(access, name, desc, signature, value);
    }

    private boolean staticAccess(int access) {
        return (access & ACC_STATIC) == ACC_STATIC;
    }

    private int signatureToBytes(String desc) {
        if (desc == null) return 0;
        switch (desc.charAt(0)) {
            case 'Z':
                return 1; // BOOLEAN
            case 'C':
                return 2; // CHAR
            case 'F':
                return 4; // FLOAT
            case 'D':
                return 8; // DOUBLE
            case 'B':
                return 1; // BYTE
            case 'S':
                return 2; // SHORT
            case 'I':
                return 4; // INT
            case 'J':
                return 8; // LONG
            case 'L':
                return 4; // OBJECT
            case '[':
                return 4; // ARRAY
            default:
                return 0; // Undefined, shouldn't happen.
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        
        if(constructor(name)) {
            if ( synthetic(access) ) {// if method is a synthetic initializer, we don't want the class initializer to be injected twice      
                return super.visitMethod(access, name, desc, signature, exceptions);
            } 
        }
        
        // get the delegate method visitor
        final MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        
        // wrap it 
        return new SiO2MethodVisitor(mv, desc, className, name, enabledClass, access, methodsIgnoredByTracking);
    }

    private boolean constructor(String name) {
        return name.equals("<init>");
    }

    private boolean synthetic(int access) {
        return (access & ACC_SYNTHETIC) == ACC_SYNTHETIC;
    }

    @Override
    public void visitEnd() {
        
        // add the size of the class to the cache
        ThreadTrackingContext.setFieldSize(className.replace('/', '.'), fieldByteCount);
        
        super.visitEnd();
    }
}
