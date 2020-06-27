package io.github.ivvve.luckmoney.common.exceptions;

public class ValidationFailedException extends IllegalArgumentException {
    public ValidationFailedException() {
        super("Validation failed");
    }

    public ValidationFailedException(final String s) {
        super(s);
    }

    public ValidationFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ValidationFailedException(final Throwable cause) {
        super(cause);
    }
}
