package com.rdoo.netflixstack.imageservice.image;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    public void test() {
        
    }
    // @Autowired
    // private UserRepository userRepository;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // public User register(User user) {
    //     user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    //     user.setRoles(new HashSet<>(Arrays.asList("USER")));

    //     return this.userRepository.insert(user);
    // }

    // public List<User> getAll() {
    //     return this.userRepository.findAll();
    // }

    // public Optional<User> getById(String id) {
    //     return this.userRepository.findById(id);
    // }

    // public Optional<User> update(String id, User user) {
    //     return this.userRepository.findById(id).map(userToUpdate -> {
    //         userToUpdate.setUsername(user.getUsername());
    //         userToUpdate.setFirstName(user.getFirstName());
    //         userToUpdate.setLastName(user.getLastName());
    //         userToUpdate.setPassword(this.passwordEncoder.encode(user.getPassword()));

    //         return this.userRepository.save(userToUpdate);
    //     });
    // }

    // public Optional<User> delete(String id) {
    //     return this.userRepository.findById(id).map(userToDelete -> {
    //         this.userRepository.deleteById(id);
    //         return userToDelete;
    //     });
    // }
}