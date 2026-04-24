package com.example.vacancyparser.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vacancyParserOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vacancy Parser API")
                        .description("API for importing Techstars vacancies and reading saved vacancies from DB")
                        .contact(new Contact().name("Vacancy Parser"))
                        .license(new License().name("Test task review")));
    }
}
