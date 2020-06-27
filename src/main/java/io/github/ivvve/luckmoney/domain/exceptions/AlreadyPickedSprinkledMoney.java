package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.StateConflictException;

public class AlreadyPickedSprinkledMoney extends StateConflictException {
    public AlreadyPickedSprinkledMoney() {
        super("User already picked sprinkled money");
    }

    public AlreadyPickedSprinkledMoney(final String message) {
        super(message);
    }

    public AlreadyPickedSprinkledMoney(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AlreadyPickedSprinkledMoney(final Throwable cause) {
        super(cause);
    }
}
