package com.rdoo.netflixstack.apigateway;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;

@Component
public class PublicPaths {
    private String loginPath;
    private String refreshTokenPath;
    private String registerPath;

    @Autowired
    private ZuulProperties zuulProperties;

    @PostConstruct
    private void init() {
        String apiPrefix = this.zuulProperties.getPrefix();
        this.loginPath = apiPrefix + "/login";
        this.refreshTokenPath = apiPrefix + "/refresh-token";
        this.registerPath = apiPrefix + "/users/register";
    }

    public String getLoginPath() {
        return loginPath;
    }

    public String getRefreshTokenPath() {
        return refreshTokenPath;
    }

    public String getRegisterPath() {
        return registerPath;
    }
}