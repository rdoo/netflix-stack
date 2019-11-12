package com.rdoo.netflixstack.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(ApiGateway.class, args);
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
}