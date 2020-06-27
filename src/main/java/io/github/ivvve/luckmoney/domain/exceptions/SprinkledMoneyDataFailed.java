package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.DatabaseErrorException;

public class SprinkledMoneyDataFailed extends DatabaseErrorException {
    public SprinkledMoneyDataFailed() {
        super("Sprinkled money data failed");
    }

    public SprinkledMoneyDataFailed(final String message) {
        super(message);
    }

    public SprinkledMoneyDataFailed(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SprinkledMoneyDataFailed(final Throwable cause) {
        super(cause);
    }

    public SprinkledMoneyDataFailed(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
