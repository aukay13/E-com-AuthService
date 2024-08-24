package com.ecommerce.authenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

}
