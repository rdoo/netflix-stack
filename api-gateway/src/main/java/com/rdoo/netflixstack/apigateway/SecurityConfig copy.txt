package com.rdoo.netflixstack.apigateway;

import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // @Value("${zuul.prefix}")
    // private String apiPrefix;

    @Autowired
	private ZuulProperties zuulProperties;

    // @Autowired
    // private OAuth2SsoProperties oAuth2SsoProperties;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.sso")
    public OAuth2SsoProperties sso() {
        return new OAuth2SsoProperties();
    }

    // @Override
    // public void configure(ResourceServerSecurityConfigurer config) {
    //     config.tokenServices(tokenServices());
    // }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String apiPrefix = this.zuulProperties.getPrefix();
        System.out.println("##########################33");
        System.out.println(this.sso().getLoginPath());
        // http.authorizeRequests().anyRequest().denyAll().and().formLogin().disable();
        // http.csrf().disable().formLogin().disable().authorizeRequests().antMatchers(apiPrefix + "/auth/**", apiPrefix + "/users/register").permitAll().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable().httpBasic().disable().authorizeRequests().anyRequest().permitAll()
        .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ResourceOwnerPasswordResourceDetails clientDetails() {
        return new ResourceOwnerPasswordResourceDetails();
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(false);
        // tokenServices.setSupportRefreshToken(true);
        // tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    @Bean
    public OAuth2RestOperations oAuth2RestTemplateBean() {
        ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        // System.out.println(tokenProvider.obtainAccessToken());
        // tokenProvider.setInterceptors(Collections.singletonList(loadBalancerInterceptor));

        OAuth2RestTemplate oauth2Template = new OAuth2RestTemplate(clientDetails(), oauth2ClientContext);
        // oauth2Template.setInterceptors(Collections.singletonList(loadBalancerInterceptor));
        oauth2Template.setAccessTokenProvider(new AccessTokenProviderChain(Collections.singletonList(tokenProvider)));

        return oauth2Template;
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter = new OAuth2ClientAuthenticationProcessingFilter(sso().getLoginPath());
        oAuth2ClientAuthenticationProcessingFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(sso().getLoginPath(), HttpMethod.POST.name()));
        // oAuth2ClientAuthenticationProcessingFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/index"));


        // AuthenticationSussHa
        // InMemoTokenS
        // oAuth2ClientAuthenticationProcessingFilter.


        oAuth2ClientAuthenticationProcessingFilter.setAuthenticationSuccessHandler(new SuccessHandlerr());

        OAuth2RestOperations restTemplate = oAuth2RestTemplateBean();

        oAuth2ClientAuthenticationProcessingFilter.setRestTemplate(restTemplate);

        // System.out.println("###############################");
        // System.out.println(restTemplate.getAccessToken().getValue());

        // UserInfoTokenServices tokenServices = new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
        oAuth2ClientAuthenticationProcessingFilter.setTokenServices(tokenServices());
        return oAuth2ClientAuthenticationProcessingFilter;
    }

    // @Bean
	// public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
	// 	FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
	// 	registration.setFilter(filter);
	// 	registration.setOrder(-100);
	// 	return registration;
    // }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
}
*/