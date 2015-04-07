package sio2box.agent;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.T_BOOLEAN;
import static org.objectweb.asm.Opcodes.T_BYTE;
import static org.objectweb.asm.Opcodes.T_CHAR;
import static org.objectweb.asm.Opcodes.T_DOUBLE;
import static org.objectweb.asm.Opcodes.T_FLOAT;
import static org.objectweb.asm.Opcodes.T_INT;
import static org.objectweb.asm.Opcodes.T_LONG;
import static org.objectweb.asm.Opcodes.T_SHORT;
import lombok.SneakyThrows;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import sio2box.api.Util;

public class MethodUtils {

    static final Method SIZE_OF_CLASS = getMethod(Util.class, "sizeOfClass", Class.class);
    static final Type UTIL = Type.getType(Util.class);
    static final String INIT = "<init>";
    static final int T_REF = 12;
	
    static int signatureToBytes(String desc) {
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
    
    static boolean staticAccess(int access) {
        return (access & ACC_STATIC) == ACC_STATIC;
    }
    
    static boolean constructor(String name) {
        return name.equals(INIT);
    }

    static boolean synthetic(int access) {
        return (access & ACC_SYNTHETIC) == ACC_SYNTHETIC;
    }
    

    @SneakyThrows
    static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return Method.getMethod(clazz.getMethod(name, parameterTypes));
    }

    static int sizeOfType(int operand) {
        switch (operand) {
            case T_BOOLEAN:
                return 1; // T_BOOLEAN: 1 byte.
            case T_CHAR:
                return 2; // T_CHAR: 2 bytes.
            case T_FLOAT:
                return 4; // T_FLOAT: 4 bytes.
            case T_DOUBLE:
                return 8; // T_DOUBLE: 8 bytes.
            case T_BYTE:
                return 1; // T_BYTE: 1 byte.
            case T_SHORT:
                return 2; // T_SHORT: 2 bytes.
            case T_INT:
                return 4; // T_INT: 4 bytes.
            case T_LONG:
                return 8; // T_LONG: 8 bytes.
            case T_REF:
                return 4; // T_REF: 4 bytes.
            default:
                return 0;
        }
    }

}
