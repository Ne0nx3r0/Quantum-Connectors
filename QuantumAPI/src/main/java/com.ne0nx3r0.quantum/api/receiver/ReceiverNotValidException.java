package com.ne0nx3r0.quantum.api.receiver;

public final class ReceiverNotValidException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ReceiverNotValidException() {
        super("This Receiver in't compatible with that type of Block.");
    }
}
