package com.stwn.ecommerce_java.common.errors;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message) {
        super(message);
    }
}
