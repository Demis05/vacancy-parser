package com.example.vacancyparser.api.dto;

public record ImportResponse(
        int retrieved,
        int created,
        int updated,
        int removed
) {
}
