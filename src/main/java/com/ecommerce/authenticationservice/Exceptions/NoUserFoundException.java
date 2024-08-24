package com.ecommerce.authenticationservice.Exceptions;

public class NoUserFoundException extends Exception{
    public NoUserFoundException(String errorMessage){
        super(errorMessage);
    }
}
