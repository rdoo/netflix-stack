package com.rdoo.netflixstack.imageservice.image;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {}