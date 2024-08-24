package com.ecommerce.authenticationservice.DTOs;
import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String status;
    private String message;
}
