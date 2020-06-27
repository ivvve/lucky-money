package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;

public class InvalidMoneys extends ValidationFailedException {
    public InvalidMoneys() {
        super("Invalid moneys");
    }

    public InvalidMoneys(final String message) {
        super(message);
    }

    public InvalidMoneys(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidMoneys(final Throwable cause) {
        super(cause);
    }
}
