package com.rdoo.netflixstack.authserver;

import com.rdoo.netflixstack.authserver.authuser.AuthUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

// default configuration: https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/spring-security-oauth2-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/security/oauth2/authserver/OAuth2AuthorizationServerConfiguration.java
// docs: https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/docs/src/docs/asciidoc/index.adoc
@Configuration
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
    AuthUserDetailsService authUserDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
        // .tokenStore(tokenStore()) // ustawia sie samoczynnie na JwtTokenStore jesli istnieje JwtAccessTokenConverter
        .accessTokenConverter(accessTokenConverter())
        .authenticationManager(authenticationManager) // required for password grant
                .userDetailsService(authUserDetailsService) // required for refresh_token grant TODO check if needed with JWT
                .reuseRefreshTokens(false); // generate new refresh token after previous was used
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
        // security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()"); // TODO needed?
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        ClientDetails client = this.clientDetails();
        clients.inMemory().withClient(client.getClientId()).secret(client.getClientSecret())
                .authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]))
                .scopes(client.getScope().toArray(new String[0])); // TODO add ROLE?
    }

    // @Bean
    // public TokenStore tokenStore() {
    //     return new JwtTokenStore(accessTokenConverter());
    // }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    // @Bean
    // @Primary
    // public DefaultTokenServices tokenServices() {
    //     DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    //     defaultTokenServices.setTokenStore(tokenStore());
    //     defaultTokenServices.setSupportRefreshToken(true);
    //     return defaultTokenServices;
    // }
}