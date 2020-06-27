package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.StateConflictException;

public class CannotPickSprinkledMoney extends StateConflictException {
    public CannotPickSprinkledMoney() {
        super("The user cannot pick the sprinkled money");
    }

    public CannotPickSprinkledMoney(final String message) {
        super(message);
    }

    public CannotPickSprinkledMoney(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CannotPickSprinkledMoney(final Throwable cause) {
        super(cause);
    }
}
