package com.example.vacancyparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VacancyParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacancyParserApplication.class, args);
    }
}
