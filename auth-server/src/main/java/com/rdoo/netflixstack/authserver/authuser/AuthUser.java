package com.rdoo.netflixstack.authserver.authuser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document("users")
public class AuthUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Set<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public List<GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(this.roles).orElse(Collections.emptySet()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
}