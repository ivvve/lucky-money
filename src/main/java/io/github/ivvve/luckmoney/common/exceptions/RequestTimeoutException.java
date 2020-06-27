package io.github.ivvve.luckmoney.common.exceptions;

public class RequestTimeoutException extends IllegalStateException {
    public RequestTimeoutException() {
        super("Request timeout");
    }

    public RequestTimeoutException(final String message) {
        super(message);
    }

    public RequestTimeoutException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RequestTimeoutException(final Throwable cause) {
        super(cause);
    }
}
