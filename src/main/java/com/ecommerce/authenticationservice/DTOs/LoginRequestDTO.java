package com.ecommerce.authenticationservice.DTOs;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
