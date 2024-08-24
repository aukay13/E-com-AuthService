package com.ecommerce.authenticationservice.Exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(String errorMessage){
        super(errorMessage);
    }
}
