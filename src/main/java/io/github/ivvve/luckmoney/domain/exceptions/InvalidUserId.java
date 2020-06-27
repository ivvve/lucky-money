package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;

public class InvalidUserId extends ValidationFailedException {
    public InvalidUserId() {
        super("Invalid User ID");
    }

    public InvalidUserId(final String message) {
        super(message);
    }

    public InvalidUserId(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidUserId(final Throwable cause) {
        super(cause);
    }
}
