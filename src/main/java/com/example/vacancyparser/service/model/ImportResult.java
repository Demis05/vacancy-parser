package com.example.vacancyparser.service.model;

public record ImportResult(
        int retrieved,
        int created,
        int updated,
        int removed
) {
}
