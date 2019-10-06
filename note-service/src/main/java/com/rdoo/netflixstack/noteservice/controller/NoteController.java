package com.rdoo.netflixstack.noteservice.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rdoo.netflixstack.noteservice.model.Note;
import com.rdoo.netflixstack.noteservice.repository.NoteRepository;

@RestController
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public List<Note> getAll() {
        return this.noteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return this.noteRepository.findById(id).map(note -> ResponseEntity.ok(note))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Note note) {
        Note createdNote = this.noteRepository.insert(note);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdNote.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Note note) {
        return this.noteRepository.findById(id).map(noteToUpdate -> {
            noteToUpdate.setTitle(note.getTitle());
            noteToUpdate.setDescription(note.getDescription());
            this.noteRepository.save(noteToUpdate);

            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}