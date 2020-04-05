package com.rdoo.netflixstack.fileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FileServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(FileServiceApp.class, args);
    }
}