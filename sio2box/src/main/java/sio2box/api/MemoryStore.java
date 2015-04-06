package sio2box.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level=AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MemoryStore {

    @Getter
    final long maxMemory;

    @Getter
    long usedMemory;

    public MemoryStore() {
        maxMemory = -1;
    }

    public void addMemory(long memory) {
        usedMemory += memory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }
}
