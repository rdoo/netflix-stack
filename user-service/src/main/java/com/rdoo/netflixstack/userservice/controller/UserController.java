package com.rdoo.netflixstack.userservice.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.rdoo.netflixstack.userservice.model.User;
import com.rdoo.netflixstack.userservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // TODO move to service

    @GetMapping
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return this.userRepository.findById(id).map(note -> ResponseEntity.ok(note))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        User createdUser = this.userRepository.insert(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        try {
            this.userRepository.insert(user);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given login already exists");
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody User user) {
        return this.userRepository.findById(id).map(userToUpdate -> {
            userToUpdate.setLogin(user.getLogin());
            this.userRepository.save(userToUpdate);

            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}