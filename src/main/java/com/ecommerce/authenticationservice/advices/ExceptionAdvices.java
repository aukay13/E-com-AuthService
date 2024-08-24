package com.ecommerce.authenticationservice.advices;

import com.ecommerce.authenticationservice.DTOs.ErrorResponseDTO;
import com.ecommerce.authenticationservice.Exceptions.IncorrectPasswordException;
import com.ecommerce.authenticationservice.Exceptions.InvalidTokenException;
import com.ecommerce.authenticationservice.Exceptions.TokenExpiredException;
import com.ecommerce.authenticationservice.Exceptions.UserAlreadyLoggedOutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.InvalidClassException;

@RestControllerAdvice
public class ExceptionAdvices {
    private final ErrorResponseDTO errorResponseDTO;

    public ExceptionAdvices(ErrorResponseDTO errorResponseDTO) {
        this.errorResponseDTO = errorResponseDTO;
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleIncorrectPasswordException(IncorrectPasswordException e){
        errorResponseDTO.setMessage(e.getMessage());
        errorResponseDTO.setStatus("Password error");
        return errorResponseDTO;
    }


    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleInvalidTokenException(InvalidTokenException e){
        errorResponseDTO.setMessage(e.getMessage());
        errorResponseDTO.setStatus("Token error");
        return errorResponseDTO;
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleTokenExpiredException(TokenExpiredException e){
        errorResponseDTO.setMessage(e.getMessage());
        errorResponseDTO.setStatus("Token expired error");
        return errorResponseDTO;
    }

    @ExceptionHandler(UserAlreadyLoggedOutException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleUserAlreadyLoggedOutException(UserAlreadyLoggedOutException e){
        errorResponseDTO.setMessage(e.getMessage());
        errorResponseDTO.setStatus("User logged out");
        return errorResponseDTO;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleException(Exception e){
        errorResponseDTO.setMessage(e.getMessage());
        errorResponseDTO.setStatus("Error");
        return errorResponseDTO;
    }
}