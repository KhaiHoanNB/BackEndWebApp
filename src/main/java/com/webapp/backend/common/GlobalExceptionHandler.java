package com.webapp.backend.common;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final org.apache.logging.log4j.Logger LOGGER =  LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {

        LOGGER.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {

        LOGGER.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The contend is not existed");

    }

    @ExceptionHandler(TooLargeQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTooLargeQuantityException(TooLargeQuantityException e) {

        LOGGER.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The quantity is too large");

    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleValidateException(ConstraintViolationException e) {

        LOGGER.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Data is not suitable");

    }

}
