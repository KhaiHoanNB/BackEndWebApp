package com.webapp.backend.common;

public class TooLargeQuantityException extends Exception{

    public TooLargeQuantityException(String message) {
        super(message);
    }
}
