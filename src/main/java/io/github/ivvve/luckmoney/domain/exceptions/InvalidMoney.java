package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;

public class InvalidMoney extends ValidationFailedException {
    public InvalidMoney() {
        super("Money is invalid");
    }

    public InvalidMoney(final String message) {
        super(message);
    }

    public InvalidMoney(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidMoney(final Throwable cause) {
        super(cause);
    }
}
