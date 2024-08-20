package com.vision.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vision.api.config.AppConfig;


//@EnableJpaRepositories
@OpenAPIDefinition(
        info = @Info(
                title = "Author-Book-Service",
                version = "1.0",
                description = "Swagger Documentation for Author Book services",
                contact = @Contact(url = "https://github.com/alokebd", name = "Aloke Das")))
@SpringBootApplication
public class SpringAuthorRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }
}
