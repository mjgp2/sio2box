package sio2box.api;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import sio2box.agent.ThreadTrackingData;

/**
 * Provides a Tracking context to allow data to be store per thread for events happening across SIO2
 * annotated classes
 *
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThreadTrackingContext {

    private static final boolean LOGGING = System.getProperty("agent.logging") != null;

    /**
     * the ThreadLocal containing the ThreadTrackingData object
     */
    static ThreadLocal<ThreadTrackingData> threadContext = new ThreadLocal<ThreadTrackingData>() {

        @Override
        protected ThreadTrackingData initialValue() {
            ThreadTrackingData threadTrackingData = new ThreadTrackingData();
            return threadTrackingData;
        }

    };

    static ConcurrentHashMap<String, Integer> fieldSizeCacheMap = new ConcurrentHashMap<String, Integer>();
    static ConcurrentHashMap<String, Integer> paddedSizes = new ConcurrentHashMap<String, Integer>();

    /**
     * checks whether we are currently tracking memory
     * 
     * @return
     */
    public static boolean currentlyTracking() {
        if (threadContext == null) {
            return false;
        }
        return currentlyTracking(getThreadContext());
    }

    /**
     * checks whether we are currently tracking memory helper method for when threadTrackingData
     * exists
     * 
     * @param threadTrackingData
     * @return
     */
    private static boolean currentlyTracking(ThreadTrackingData threadTrackingData) {
        return threadTrackingData != null && threadTrackingData.getNumDeep() > 0 && threadTrackingData.getIgnoredNumDeep() == 0;
    }

    public static void addArrayBytes(int size) {
        if ( logging() ) log("Array bytes: {0}",size);
        addBytes(size);
    }


    private static boolean logging() {
        if ( ! LOGGING ) return false;
        ThreadTrackingData threadTrackingData = getThreadContext();
        return currentlyTracking(threadTrackingData);
    }

    public static void addMultiArrayBytes(int size) {
        if ( logging() ) log("MultiArray bytes: {0}", size);
        addBytes(size);
    }

    public static void addCloneBytes(Object o) {
        // clone is a shallow copy (normally)
        addBytesFromFields(o);
    }

    /**
     * Records bytes given by size.
     * 
     * @param size
     */
    public static void addBytes(int size) {
        if (size == 0) {
            return;
        }
        if (threadContext == null) {
            return;
        }

        ThreadTrackingData threadTrackingData = getThreadContext();
        if (!currentlyTracking(threadTrackingData)) {
            return;
        }

        addBytes(size, threadTrackingData);
    }

    /**
     * records bytes given by size helper method for when threadTrackingData exists
     * 
     * @param size
     * @param threadTrackingData
     */
    private static void addBytes(int size, ThreadTrackingData threadTrackingData) {

        if (size == 0) {
            return;
        }


        if ( LOGGING ) log("Tracking bytes: {0}",size);
        threadTrackingData.increaseBytesUsed(size);
        checkExceeded(threadTrackingData);
    }

    private static void log(String pattern, int argument) {
        ThreadTrackingData context = getThreadContext();
        context.incrementIgnoredNumDeep();
        try {
            System.err.println(MessageFormat.format(pattern, argument));
            System.err.flush();
        } finally {
            context.decrementIgnoredNumDeep();
        }
    }

    private static void checkExceeded(ThreadTrackingData threadTrackingData) {
        final long maxMemory = threadTrackingData.getMaxMemory();
        if (maxMemory >= 0 && (threadTrackingData.getBytesUsed() + threadTrackingData.getInitialMemory()) > maxMemory) {
            threadTrackingData.setMaxMemory(-1);
            throw new MemoryExceededException(maxMemory, threadTrackingData.getBytesUsed() + threadTrackingData.getInitialMemory());
        }
    }

    /**
     * record bytes taken by object given by className
     * 
     * @param clazz
     */
    public static void addBytesFromFields(Object o) {
        if (threadContext == null) {
            return;
        }

        ThreadTrackingData threadTrackingData = getThreadContext();
        if (!currentlyTracking(threadTrackingData)) {
            return;
        }

        startIgnoring(threadTrackingData);
        Class<?> clazz = o.getClass();
        Integer paddedBytes;
        try {
            paddedBytes = paddedSizes.get(clazz.getName());
            if (paddedBytes == null) {
                paddedBytes = getPaddedBytes(threadTrackingData, clazz);
                paddedSizes.put(clazz.getName(), paddedBytes);
            }
        } finally {
            stopIgnoring(threadTrackingData);
        }
        addBytes(paddedBytes, threadTrackingData);
    }

    public static ThreadTrackingData getThreadContext() {
        return threadContext.get();
    }

    /**
     * calculates the size of object given by className
     * 
     * @param threadTrackingData
     * 
     * @param className
     * @return
     */
    private static int getPaddedBytes(ThreadTrackingData threadTrackingData, Class<?> clazz) {
        int bytes = 8; // Object overhead is 8 bytes.
        threadContext.remove();
        try {
            Integer currentBytes;
            String className = clazz.getName();
            while (clazz != Object.class) {
                try {
                    currentBytes = ThreadTrackingContext.fieldSizeCacheMap.get(className);
                    if (currentBytes == null) {
                        // if it hasn't been instrumented, it's been ignored on purpose
                        continue;
                    }
                    bytes += currentBytes;
                } finally {
                    clazz = clazz.getSuperclass();
                    className = clazz.getName();
                }
            }
            bytes = Util.padded(bytes);
        } finally {
            threadContext.set(threadTrackingData);
        }
        return bytes;
    }

    public static long getBytes() {
        return getThreadContext().getBytesUsed();
    }

    /**
     * starts ignoring addBytes calls
     */
    public static void startIgnoring() {
        startIgnoring(getThreadContext());
    }

    /**
     * starts ignoring addBytes calls helper method for when threadTrackingData exists
     * 
     * @param threadTrackingData
     */
    private static void startIgnoring(ThreadTrackingData threadTrackingData) {
        threadTrackingData.incrementIgnoredNumDeep();
    }

    /**
     * stops ignoring addBytes calls
     */
    public static void stopIgnoring() {
        stopIgnoring(getThreadContext());
    }

    /**
     * stops ignoring addBytes calls helper method for when threadTrackingData exists
     * 
     * @param threadTrackingData
     */
    private static void stopIgnoring(ThreadTrackingData threadTrackingData) {
        threadTrackingData.decrementIgnoredNumDeep();
    }


    /**
     * start the tracker
     */
    public static void startTracker(long initialMemory, long maxMemory) {
        ThreadTrackingData threadTrackingData = getThreadContext();
        // if this is the first time start tracker is called
        if (threadTrackingData.getNumDeep() == 0) {
            threadTrackingData.setMaxMemory(maxMemory);
            threadTrackingData.setBytesUsed(0);
            threadTrackingData.setInitialMemory(initialMemory);
            threadTrackingData.setIgnoredNumDeep(0);
        }
        threadTrackingData.incrementNumDeep();
    }

    /**
     * stops the tracker
     */
    public static void stopTracker() {
        ThreadTrackingData threadTrackingData = getThreadContext();
        threadTrackingData.decrementNumDeep();
    }

    /**
     * clears the tracker
     */
    public static void clear() {
        ThreadTrackingData threadTrackingData = getThreadContext();
        if (threadTrackingData.getNumDeep() == 0) {
            threadTrackingData.setBytesUsed(0);
            threadTrackingData.setInitialMemory(0);
        }
    }

    public static void setFieldSize(String clazz, int bytes) {
        fieldSizeCacheMap.put(clazz, bytes);
    }
}
