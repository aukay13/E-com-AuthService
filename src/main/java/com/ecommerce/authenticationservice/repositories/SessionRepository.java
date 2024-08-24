package com.ecommerce.authenticationservice.repositories;

import com.ecommerce.authenticationservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Long> {

    @Override
    Session save(Session session);

    Session findByUser_Id(Long id);

    Session findByToken(String token);
}
