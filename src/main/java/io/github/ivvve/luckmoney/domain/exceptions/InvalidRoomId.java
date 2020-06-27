package io.github.ivvve.luckmoney.domain.exceptions;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;

public class InvalidRoomId extends ValidationFailedException {
    public InvalidRoomId() {
        super("Invalud Room ID");
    }

    public InvalidRoomId(final String message) {
        super(message);
    }

    public InvalidRoomId(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidRoomId(final Throwable cause) {
        super(cause);
    }
}
