package com.rdoo.netflixstack.apigateway;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // @Autowired
    // private PublicPaths publicPaths;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // http.authorizeRequests().antMatchers(this.publicPaths.getLoginPath(), this.publicPaths.getRefreshTokenPath(),
        //         this.publicPaths.getRegisterPath()).permitAll().anyRequest().authenticated();
        http.authorizeRequests().anyRequest().permitAll(); // TODO
    }
}