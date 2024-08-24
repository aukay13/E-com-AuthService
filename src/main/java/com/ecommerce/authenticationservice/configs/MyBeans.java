package com.ecommerce.authenticationservice.configs;

import com.ecommerce.authenticationservice.DTOs.ErrorResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MyBeans {

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ErrorResponseDTO getErrorResponseDTO(){
        return new ErrorResponseDTO();
    }
}
