package com.example.vacancyparser.service.model;

public record VacancyFilter(
        String company,
        String location,
        String tag
) {
}
