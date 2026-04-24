package com.example.vacancyparser.config;

public record TechstarsHttpProperties(
        int timeoutMillis,
        int detailDelayMillis,
        int detailThreadCount,
        String userAgent) { }
