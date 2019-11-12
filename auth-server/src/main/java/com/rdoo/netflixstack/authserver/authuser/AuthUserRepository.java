package com.rdoo.netflixstack.authserver.authuser;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByLogin(String username);
}