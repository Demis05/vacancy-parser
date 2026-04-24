package com.example.vacancyparser.exception;

public class VacancyNotFoundException extends RuntimeException {

    public VacancyNotFoundException(Long id) {
        super("Vacancy with id=%d was not found".formatted(id));
    }
}
