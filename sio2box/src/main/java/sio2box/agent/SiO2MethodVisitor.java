package sio2box.agent;

import static sio2box.agent.MethodUtils.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import sio2box.annotations.SiO2Method;
import sio2box.api.MemoryStore;
import sio2box.api.ThreadTrackingContext;
import sio2box.api.Util;

/**
 * TODO: Interface static method support
 *
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiO2MethodVisitor extends AdviceAdapter {

    static final String CONTEXT_INTERNAL_NAME = Type.getInternalName(ThreadTrackingContext.class);
    static final String MEMORYSTORE_INTERNAL_NAME = Type.getInternalName(MemoryStore.class);
    static final String INITIALIZEMEMORYSTORE_SIGNATURE = "(L" + MEMORYSTORE_INTERNAL_NAME + ";)L" + MEMORYSTORE_INTERNAL_NAME + ";";
    static final String UTIL_INTERNAL_NAME = Type.getInternalName(Util.class);
    static final String MEMORYSTORE_DESCRIPTOR = Type.getDescriptor(MemoryStore.class);
    static final String METHOD_ANNOTATION = Type.getDescriptor(SiO2Method.class);

    @Getter
    boolean trackedMethod = false;

    String className;
    String methodName;

    boolean memoryStore = false;

    boolean enabledClass;
    String desc;
    int access;
    List<String> methodsIgnoredByTracking;

    boolean ignoredMethod;

    Label startFinally;

    public SiO2MethodVisitor(MethodVisitor methodVisitor, String desc, String className, String methodName, boolean enabledClass, int access, List<String> methodsIgnoredByTracking) {
        super(ASM5, methodVisitor, access, methodName, desc);
        this.enabledClass = enabledClass;
        this.methodsIgnoredByTracking = methodsIgnoredByTracking;
        this.access = access;
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
        this.ignoredMethod = methodIgnoredByTracking();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        // need class *and* method annotation
        if (enabledClass && desc.equals(METHOD_ANNOTATION)) {
            trackedMethod = true;
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (ignoredMethod && trackedMethod) {
            System.err.println("Method both ignored and annotated! " + className + "#" + methodName);
            return;
        }

        if (ignoredMethod) {
            startIgnoring();
        }

        if (trackedMethod) {
            startTracker();
        }

        if (ignoredMethod || trackedMethod) {
            // add a new label for our finally call at the end to ensure we always add the bytes
            startFinally = new Label();
            mv.visitLabel(startFinally);
        }
    }


    @Override
    @SneakyThrows
    public void visitMethodInsn(int opcode, String owner, String name, String signature, boolean itf) {

        // track creating arrays by reflection as they don't produce the same opcodes
        if (opcode == INVOKESTATIC && owner.equals("java/lang/reflect/Array") && name.equals("newInstance")) {

            if (signature.equals("(Ljava/lang/Class;I)Ljava/lang/Object;")) {
                onSingleDimensionArrayNewInstance();
            } else if (signature.equals("(Ljava/lang/Class;[I)Ljava/lang/Object;")) {
                onMultiDimensionArrayNewInstance();
            }

            super.visitMethodInsn(opcode, owner, name, signature, itf);
            return;
        }

        if (opcode == INVOKEVIRTUAL && name.equals("clone") && owner.startsWith("[")) {
            super.visitMethodInsn(opcode, owner, name, signature, itf);
            onArrayClone(owner);
            return;
        }

        if (opcode == INVOKESPECIAL && owner.equals("java/lang/Object")) {

            // track calls to Object.new
            if (constructor(name)) {

                // finish initializing the object
                super.visitMethodInsn(opcode, owner, name, signature, itf);

                // add bytes to tracker
                addBytesFromFields();

                return;
            }

            if (name.equals("clone")) {
                super.visitMethodInsn(opcode, owner, name, signature, itf);
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addCloneBytes", "(Ljava/lang/Object;)V", false);
                // -> stack:
                return;
            }

        }

        super.visitMethodInsn(opcode, owner, name, signature, itf);
    }

    private void onArrayClone(String owner) {
        int dims = getDimensions(owner);
        if (dims > 1) {
            onMultiDimensionArrayClone(owner, dims);
        } else {
            onSingleDimensionArrayClone(owner, dims);
        }
    }

    private int getDimensions(String owner) {
        int i = 0;
        while (i < owner.length()) {
            if (owner.charAt(i) != '[') {
                break;
            }
            i++;
        }
        return i;
    }

    private void onSingleDimensionArrayClone(String owner, int i) {
        // -> stack: newobj
        mv.visitInsn(DUP);
        // -> ... newobj newobj
        mv.visitTypeInsn(CHECKCAST, owner);
        // -> ... newobj arrayref
        mv.visitInsn(ARRAYLENGTH);
        // -> ... newobj length
        mv.visitIntInsn(SIPUSH, sizeOfType(typeFromChar(owner.charAt(i))));
        // -> ... newobj length size
        mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForArray", "(II)I", false);
        // -> ... newobj bytes
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addArrayBytes", "(I)V", false);
        // -> stack: newobj
    }

    private void onMultiDimensionArrayClone(String owner, int i) {
        int numDims = i;
        // -> stack: newobj
        mv.visitInsn(DUP);
        // -> ... newobj newobj
        mv.visitTypeInsn(CHECKCAST, owner);
        // -> ... newobj arrayRef
        mv.visitIntInsn(BIPUSH, numDims);
        // -> ... newobj arrayRef numDims
        mv.visitIntInsn(NEWARRAY, T_INT);
        // -> ... newobj arrayRef lengtharrayRef
        for (i = 0; i < numDims; i++) {
            mv.visitInsn(DUP_X1);
            // -> ... lengthArrayRef arrayRef lengthArrayRef
            mv.visitInsn(SWAP);
            // -> ... lengthArrayRef lengthArrayRef arrayRef
            mv.visitInsn(DUP_X1);
            // -> ... lengthArrayRef arrayRef lengthArrayRef arrayRef
            mv.visitInsn(ARRAYLENGTH);
            // -> ... lengthArrayRef arrayRef lengthArrayRef length
            mv.visitIntInsn(BIPUSH, i);
            // -> ... lengthArrayRef arrayRef lengthArrayRef length index
            mv.visitInsn(SWAP);
            // -> ... lengthArrayRef arrayRef lengthArrayRef index length
            mv.visitInsn(IASTORE);
            // -> ... lengthArrayRef arrayRef
            if (i != numDims - 1) { // Check if not last dimension
                mv.visitInsn(ICONST_0);
                // -> ... lengthArrayRef arrayRef 0
                mv.visitInsn(AALOAD);
                // -> ... lengthArrayRef subArrayRef
                mv.visitInsn(SWAP);
                // -> ... subArrayRef lengthArrayRef
            }
        }
        mv.visitInsn(POP);
        // -> ... newobj lengthArrayRef
        mv.visitIntInsn(SIPUSH, sizeOfType(typeFromChar(owner.charAt(numDims))));
        // -> ... newobj lengthArrayRef size
        mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForMultiArray", "([II)I", false);
        // -> ... newobj bytes
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addMultiArrayBytes", "(I)V", false);
        // -> stack: newobj
    }

    private void onMultiDimensionArrayNewInstance() {
        // stack: class dimsArray
        mv.visitInsn(DUP_X1);
        // ... dimsArray class dimsArray
        mv.visitInsn(SWAP);
        // ... dimsArray dimsArray class
        mv.visitInsn(DUP_X2);
        // ... class dimsArray dimsArray class
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, UTIL_INTERNAL_NAME, "sizeOfClass", "(Ljava/lang/Class;)I", false);
        // -> class dimsArray dimsArray sizeof(T_REF)
        mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForMultiArray", "([II)I", false);
        // -> class dimsArray bytes
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addMultiArrayBytes", "(I)V", false);
        // stack: class dimsArray
    }

    private void onSingleDimensionArrayNewInstance() {
        // stack: ... class count
        dupX1();
        // ... count class count
        swap();
        // ... count count class
        dupX2();
        // ... class count count class
        invokeStatic(UTIL, SIZE_OF_CLASS);

        mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForArray", "(II)I", false);
        // -> ... count bytes

        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addArrayBytes", "(I)V", false);
        // stack: class count
    }

    private void addBytesFromFields() {

        if (!constructor(methodName)) {
            push(8);
            super.visitMethodInsn(INVOKESTATIC, "sio2box/api/ThreadTrackingContext", "addBytes", "(I)V", false);
            return;
        }

        super.loadThis();
        super.visitMethodInsn(INVOKESTATIC, "sio2box/api/ThreadTrackingContext", "addBytesFromFields", "(Ljava/lang/Object;)V", false);
    }


    private void startIgnoring() {
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "startIgnoring", "()V", false);
    }

    private void startTracker() {
        // Check if MemoryStore object is an argument, then push the memory onto the stack.
        Type[] typeArray = Type.getArgumentTypes(desc);
        if (typeArray.length > 0 && typeArray[0].getDescriptor().equals(MEMORYSTORE_DESCRIPTOR)) {

            int location = staticMethod(access) ? 0 : 1; // First location is taken by "this" if not
                                                         // static.

            memoryStore = true;

            // get the MemoryStore reference on the stack
            mv.visitVarInsn(ALOAD, location);

            // pass the reference into initialize it (will create if null)
            mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "initializeMemoryStore", INITIALIZEMEMORYSTORE_SIGNATURE, false);

            // put into local var
            mv.visitVarInsn(ASTORE, location);

            // copy back onto stack
            mv.visitVarInsn(ALOAD, location);

            // get used memory
            mv.visitMethodInsn(INVOKEVIRTUAL, MEMORYSTORE_INTERNAL_NAME, "getUsedMemory", "()J", false);

            // copy back onto stack again
            mv.visitVarInsn(ALOAD, location);

            // get max memory
            mv.visitMethodInsn(INVOKEVIRTUAL, MEMORYSTORE_INTERNAL_NAME, "getMaxMemory", "()J", false);
        } else {
            // used memory = 0
            mv.visitInsn(LCONST_0);

            // max memory = -1 (unlimited)
            mv.visitLdcInsn(Long.valueOf(-1L));
        }

        // start tracker, passing in used and max memory
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "startTracker", "(JJ)V", false);
    }

    private void stopTracker() {
        if (memoryStore) {

            // get the memory store
            int location = staticMethod(access) ? 0 : 1;
            mv.visitVarInsn(ALOAD, location);

            // get the bytes on the stack
            mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "getBytes", "()J", false);

            // add to memory
            mv.visitMethodInsn(INVOKEVIRTUAL, MEMORYSTORE_INTERNAL_NAME, "addMemory", "(J)V", false);
        }
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "stopTracker", "()V", false);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {

        if (ignoredMethod && trackedMethod) {
            System.err.println("Method both ignored and annotated! " + className + "#" + methodName);
            return;
        }

        if (trackedMethod || ignoredMethod) {
            Label endFinally = new Label();
            mv.visitTryCatchBlock(startFinally, endFinally, endFinally, null);
            mv.visitLabel(endFinally);
            onFinally();
            mv.visitInsn(ATHROW);
        }

        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodEnter() {}

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            onFinally();
        }
    }

    private void onFinally() {

        if (ignoredMethod) {
            stopIgnoring();
        }
        if (trackedMethod) {
            stopTracker();
        }
    }

    private void stopIgnoring() {
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "stopIgnoring", "()V", false);
    }



    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (opcode == ANEWARRAY) {
            // -> stack: count
            mv.visitInsn(DUP);
            // -> ... count count
            mv.visitIntInsn(SIPUSH, 4); // Object reference is 4 bytes.
            // -> ... count count num
            mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForArray", "(II)I", false);
            // -> ... count bytes
            mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addArrayBytes", "(I)V", false);
            // -> stack: count
        }

        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        if (opcode == NEWARRAY) {
            if (operand >= T_BOOLEAN && operand <= T_LONG) {
                // -> stack: count
                mv.visitInsn(DUP);
                // -> ... count count
                mv.visitIntInsn(SIPUSH, sizeOfType(operand));
                // -> ... count count num
                mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForArray", "(II)I", false);
                // -> ... count bytes
                mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addArrayBytes", "(I)V", false);
                // -> stack: count
            }
        }

        super.visitIntInsn(opcode, operand);
    }


    @Override
    public void visitMultiANewArrayInsn(String desc, int numDims) {
        // -> stack: dim1 dim2 dim3 ... dimN-1 dimN
        mv.visitIntInsn(BIPUSH, numDims);
        // -> dim1 dim2 dim3 ... dimN-1 dimN numDims
        mv.visitIntInsn(NEWARRAY, T_INT);
        // -> dim1 dim2 dim3 ... dimN-1 dimN arrayRef
        for (int i = 0; i < numDims; i++) {
            mv.visitInsn(DUP_X1);
            // -> ... dimN-1 arrayRef dimN arrayRef
            mv.visitInsn(SWAP);
            // -> ... dimN-1 arrayRef arrayRef dimN
            mv.visitIntInsn(BIPUSH, numDims - 1 - i);
            // -> ... dimN-1 arrayRef arrayRef dimN index
            mv.visitInsn(SWAP);
            // -> ... dimN-1 arrayRef arrayRef index dimN
            mv.visitInsn(IASTORE);
            // -> ... dimN-1 arrayRef
        }
        // -> arrayRef
        mv.visitInsn(DUP);
        // -> arrayRef arrayRef
        mv.visitIntInsn(SIPUSH, sizeOfType(typeOfMultiArray(desc)));
        // -> arrayRef arrayRef sizeof(T_REF)
        mv.visitMethodInsn(INVOKESTATIC, UTIL_INTERNAL_NAME, "calculateBytesForMultiArray", "([II)I", false);
        // -> arrayRef bytes
        // //log.debug("adding {}*count bytes for MULTI-NEWARRAY allocation",
        // sizeOfType(typeOfMultiArray(desc)));
        mv.visitMethodInsn(INVOKESTATIC, CONTEXT_INTERNAL_NAME, "addMultiArrayBytes", "(I)V", false);
        // -> arrayRef
        for (int i = 0; i < numDims; i++) {
            mv.visitInsn(DUP);
            // -> ... arrayRef arrayRef
            mv.visitIntInsn(BIPUSH, numDims - 1 - i);
            // -> ... arrayRef arrayRef index
            mv.visitInsn(IALOAD);
            // -> ... arrayRef dimi
            mv.visitInsn(SWAP);
            // -> ... dimi arrayRef
        }
        // -> dim1 dim2 dim3 ... dimN arrayRef
        mv.visitInsn(POP);
        // -> stack: dim1 dim2 dim3 ... dimN
        super.visitMultiANewArrayInsn(desc, numDims);
    }

    private int typeOfMultiArray(String desc) {
        int i;
        for (i = 0; i < desc.length(); i++) {
            if (desc.charAt(i) != '[') {
                break;
            }
        }
        if (i != desc.length() - 1) {
            return T_REF;
        }
        return typeFromChar(desc.charAt(i));
    }

    private int typeFromChar(char type) {
        switch (type) {
            case 'Z':
                return T_BOOLEAN;
            case 'C':
                return T_CHAR;
            case 'F':
                return T_FLOAT;
            case 'D':
                return T_DOUBLE;
            case 'B':
                return T_BYTE;
            case 'S':
                return T_SHORT;
            case 'I':
                return T_INT;
            case 'J':
                return T_LONG;
            case 'L':
                return T_REF;
            default:
                return 0;
        }
    }



    private boolean methodIgnoredByTracking() {
        final String fqMethodName = className + "." + methodName;
        for (String ignoreClass : methodsIgnoredByTracking) {
            if (fqMethodName.startsWith(ignoreClass)) {
                return true;
            }
        }
        return false;
    }
}
