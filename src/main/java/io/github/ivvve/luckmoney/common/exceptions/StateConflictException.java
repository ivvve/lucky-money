package io.github.ivvve.luckmoney.common.exceptions;

public class StateConflictException extends IllegalStateException {
    public StateConflictException() {
        super("State conflict");
    }

    public StateConflictException(final String message) {
        super(message);
    }

    public StateConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StateConflictException(final Throwable cause) {
        super(cause);
    }
}
