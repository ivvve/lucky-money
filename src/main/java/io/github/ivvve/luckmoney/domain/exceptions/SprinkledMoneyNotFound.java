package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ResourceNotFoundException;

public class SprinkledMoneyNotFound extends ResourceNotFoundException {
    public SprinkledMoneyNotFound() {
        super("Sprinkled money not found");
    }

    public SprinkledMoneyNotFound(final String message) {
        super(message);
    }

    public SprinkledMoneyNotFound(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SprinkledMoneyNotFound(final Throwable cause) {
        super(cause);
    }

    public SprinkledMoneyNotFound(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
