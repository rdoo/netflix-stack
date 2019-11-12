package com.rdoo.netflixstack.apigateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.authorizeRequests().anyRequest().denyAll().and().formLogin().disable();
        http.csrf().disable().authorizeRequests().antMatchers("/api/v1/auth/**").permitAll().anyRequest().authenticated();
        // http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }

    // @Bean
    // @ConfigurationProperties("security.oauth2.client")
    // public ResourceOwnerPasswordResourceDetails clientDetails() {
    //     return new ResourceOwnerPasswordResourceDetails();
    // }
}