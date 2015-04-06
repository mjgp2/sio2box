package sio2box.agent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * object to store the data about the thread (tracked memory usage, execution time etc)
 * 
 * @author dev
 *
 */
@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ThreadTrackingData {

    /**
     * Maximum memory allowed by method
     */
    long maxMemory;

    /**
     * Initial memory from MemoryStore method argument.
     */
    long initialMemory;

    /**
     * Current counter for bytes used
     */
    long bytesUsed;

    /**
     * This is for the number of start tracker calls
     */
    short numDeep;

    /**
     * This is for the number of ignored class
     */
    int ignoredNumDeep;

    public void increaseBytesUsed(int amount) {
        bytesUsed += amount;
    }

    public void decreaseBytesUsed(int amount) {
        bytesUsed -= amount;
    }

    public void incrementIgnoredNumDeep() {
        ignoredNumDeep++;
    }

    public void decrementIgnoredNumDeep() {
        ignoredNumDeep--;
    }

    public void incrementNumDeep() {
        numDeep++;
    }

    public void decrementNumDeep() {
        numDeep--;
    }

    public void halt() {
        numDeep = 0;
        bytesUsed = 0;
    }
}
