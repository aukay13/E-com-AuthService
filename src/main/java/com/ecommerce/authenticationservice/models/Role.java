package com.ecommerce.authenticationservice.models;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Data
@Entity
public class Role extends BaseModel{
    private String role;
}
