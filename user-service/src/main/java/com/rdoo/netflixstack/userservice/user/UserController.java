package com.rdoo.netflixstack.userservice.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.NO_CONTENT) // needed for swagger documentation
    @ApiOperation("Register new user")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "User successfully registered"),
            @ApiResponse(code = 409, message = "User with given username already exists") })
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            this.userService.register(user);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given username already exists");
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')") // TODO ADMIN
    @ApiOperation("Get all users")
    public List<User> getAll() {
        return this.userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get user by id")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<?> getById(@PathVariable String id) {
        return this.userService.getById(id).map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Update user by id")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "User succesfully updated"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody User user) {
        return this.userService.update(id, user).map(updatedUser -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete user by id")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "User succesfully deleted"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<?> delete(@PathVariable String id) {
        return this.userService.delete(id).map(deletedUser -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}