package io.github.ivvve.luckmoney.ui.config;

import io.github.ivvve.luckmoney.common.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionResponseHandler {

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<String> handleDatabaseErrorException(final DatabaseErrorException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RequestTimeoutException.class)
    public ResponseEntity<String> handleRequestTimeoutException(final RequestTimeoutException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(final ResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StateConflictException.class)
    public ResponseEntity<String> handleStateConflictException(final StateConflictException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<String> handleValidationFailedException(final ValidationFailedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobal(final ValidationFailedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
