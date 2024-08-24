package com.ecommerce.authenticationservice.Exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String errorMessage){
        super(errorMessage);
    }
}