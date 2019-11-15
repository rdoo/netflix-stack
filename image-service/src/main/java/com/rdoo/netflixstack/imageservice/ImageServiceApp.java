package com.rdoo.netflixstack.imageservice;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableMongoAuditing
//(dateTimeProviderRef = "dateTimeProvider")
public class ImageServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ImageServiceApp.class, args);
        // System.out.println(
        //     Calendar.getInstance().getTimeZone().getDisplayName());
        //     TimeZone.setDefault(Calendar.getInstance().getTimeZone());
    }

    // @Bean
    // public DateTimeProvider dateTimeProvider() {
    //     return () -> Optional.of(ZonedDateTime.now());
    // }
}