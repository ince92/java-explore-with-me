package ru.practicum.ewm_main.exception;

public class ValidationException extends IllegalStateException {
    public ValidationException(final String message) {
        super(message);
    }
}
