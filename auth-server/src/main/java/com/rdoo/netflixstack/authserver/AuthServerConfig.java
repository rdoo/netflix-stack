package com.rdoo.netflixstack.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;

@Configuration
@EnableConfigurationProperties(AuthorizationServerProperties.class)
@Import(AuthorizationServerTokenServicesConfiguration.class)
@EnableAuthorizationServer
@SuppressWarnings("deprecation")
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public BaseClientDetails clientDetails() {
        return new BaseClientDetails();
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AccessTokenConverter accessTokenConverter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager) // required for password grant
                .userDetailsService(userDetailsService) // required for refresh_token grant
                .accessTokenConverter(accessTokenConverter) // required to support JWT
                .reuseRefreshTokens(false); // generate new refresh token after previous one was used
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        ClientDetails client = this.clientDetails();
        clients.inMemory().withClient(client.getClientId()).secret(client.getClientSecret())
                .authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]))
                .scopes(client.getScope().toArray(new String[0]));
    }
}