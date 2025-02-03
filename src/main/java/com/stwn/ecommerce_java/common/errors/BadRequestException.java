package com.stwn.ecommerce_java.common.errors;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
