package com.example.vacancyparser.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "techstars")
public record TechstarsProperties(
        String baseUrl,
        String jobsPath,
        TechstarsHttpProperties http) {}
