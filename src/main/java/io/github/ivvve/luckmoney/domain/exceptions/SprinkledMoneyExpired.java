package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.RequestTimeoutException;

public class SprinkledMoneyExpired extends RequestTimeoutException {
    public SprinkledMoneyExpired() {
        super("Sprinkled money is expired");
    }

    public SprinkledMoneyExpired(final String message) {
        super(message);
    }

    public SprinkledMoneyExpired(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SprinkledMoneyExpired(final Throwable cause) {
        super(cause);
    }
}
