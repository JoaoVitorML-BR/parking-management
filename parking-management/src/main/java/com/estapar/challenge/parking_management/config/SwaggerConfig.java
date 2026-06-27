package com.estapar.challenge.parking_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parking Management API")
                        .description("API para gerenciamento de estacionamento - Desafio Técnico Estapar")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("João Vitor")
                                .email("jvmlira33@gmail.com.com")));
    }
}
