package com.rdoo.netflixstack.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rdoo.netflixstack.noteservice.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> {}