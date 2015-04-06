package sio2box.agent;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class Agent {

    /**
     * JVM hook to statically load the javaagent at startup.
     * 
     * After the Java Virtual Machine (JVM) has initialized, the premain method will be called. Then
     * the real application main method will be called.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void premain(String args, Instrumentation inst) throws Exception {

        inst.addTransformer(new SiO2ClassFileTransformer(), inst.isRetransformClassesSupported());
        
        retransformLoadedClasses(inst);
    }

    private static void retransformLoadedClasses(Instrumentation inst) throws Exception {
        List<Class<?>> classList = getAllLoadedModifiableClasses(inst);
        try {
            inst.retransformClasses(classList.toArray(new Class<?>[classList.size()]));
        } catch (Exception e) {
            System.err.println("Agent was unable to retransform early loaded classes.");
            throw e;
        }
    }

    private static List<Class<?>> getAllLoadedModifiableClasses(Instrumentation inst) {
        Class<?>[] classes = inst.getAllLoadedClasses();
        List<Class<?>> classList = new ArrayList<Class<?>>(classes.length);
        for (int i = 0; i < classes.length; i++) {
            if (inst.isModifiableClass(classes[i])) {
                classList.add(classes[i]);
            }
        }
        return classList;
    }

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * 
     * The agent class may have an agentmain method for use when the agent is started after VM
     * startup.
     * 
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) throws Exception {

        inst.addTransformer(new SiO2ClassFileTransformer());
        retransformLoadedClasses(inst);
    }
}
