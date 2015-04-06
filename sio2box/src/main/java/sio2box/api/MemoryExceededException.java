package sio2box.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("serial")
@FieldDefaults(level=AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MemoryExceededException extends RuntimeException {

    final long max;
    final long amount;

    @Override
    public String getMessage() {
        return "Memory Exceeded: " + max + " bytes by " + (amount - max) + " bytes.";
    }
}
