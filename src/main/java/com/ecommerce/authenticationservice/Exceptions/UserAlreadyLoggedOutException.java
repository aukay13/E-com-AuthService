package com.ecommerce.authenticationservice.Exceptions;

public class UserAlreadyLoggedOutException extends Exception{
    public UserAlreadyLoggedOutException(String errorMessage){
        super(errorMessage);
    }
}
