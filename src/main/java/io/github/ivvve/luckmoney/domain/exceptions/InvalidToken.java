package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;

public class InvalidToken extends ValidationFailedException {
    public InvalidToken() {
        super("Token is invalid");
    }

    public InvalidToken(final String message) {
        super(message);
    }

    public InvalidToken(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidToken(final Throwable cause) {
        super(cause);
    }
}
