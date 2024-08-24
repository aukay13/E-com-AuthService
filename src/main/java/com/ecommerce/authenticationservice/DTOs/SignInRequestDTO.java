package com.ecommerce.authenticationservice.DTOs;

import lombok.Data;

@Data
public class SignInRequestDTO {
    private String email;
    private String password;
}
