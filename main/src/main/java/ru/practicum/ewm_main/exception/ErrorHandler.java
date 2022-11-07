package ru.practicum.ewm_main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new ApiError(Collections.singletonList(Arrays.toString(e.getStackTrace())),e.getMessage(),"The required object was not found.",
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
        return new ApiError(Collections.singletonList(Arrays.toString(e.getStackTrace())),e.getMessage(),"For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now().toString());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        return new ResponseEntity<>("Произошла непредвиденная ошибка. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
