package com.rdoo.netflixstack.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

// @Configuration
// // @EnableAuthorizationServer
// public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
//     @Autowired
//     AuthenticationManager authenticationManager;

//     @Override
//     public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//         endpoints.authenticationManager(authenticationManager).reuseRefreshTokens(false); // new refresh token after using previous
//     }

//     @Override
//     public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//         oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
//     }

//     @Override
//     public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//         clients.inMemory().withClient("client").secret("{noop}secret").authorizedGrantTypes("password", "refresh_token")
//                 .scopes("all");
//     }
// }