package io.github.ivvve.luckmoney.common.exceptions;

public class DatabaseErrorException extends RuntimeException {
    public DatabaseErrorException() {
        super("Database error");
    }

    public DatabaseErrorException(final String message) {
        super(message);
    }

    public DatabaseErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DatabaseErrorException(final Throwable cause) {
        super(cause);
    }

    public DatabaseErrorException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
