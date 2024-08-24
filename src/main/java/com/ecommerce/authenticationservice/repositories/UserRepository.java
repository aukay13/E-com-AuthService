package com.ecommerce.authenticationservice.repositories;

import com.ecommerce.authenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    User save(User u);

    Optional<User> findByEmail(String email);
}
