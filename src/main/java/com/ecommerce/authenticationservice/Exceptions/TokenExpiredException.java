package com.ecommerce.authenticationservice.Exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException(String errorMessage){
        super(errorMessage);
    }
}
