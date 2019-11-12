package com.rdoo.netflixstack.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rdoo.netflixstack.userservice.model.User;

public interface UserRepository extends MongoRepository<User, String> {}