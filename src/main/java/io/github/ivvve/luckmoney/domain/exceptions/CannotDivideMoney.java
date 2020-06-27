package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.StateConflictException;

public class CannotDivideMoney extends StateConflictException {
    public CannotDivideMoney() {
        super("Cannot divide the money");
    }

    public CannotDivideMoney(final String message) {
        super(message);
    }

    public CannotDivideMoney(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CannotDivideMoney(final Throwable cause) {
        super(cause);
    }
}
