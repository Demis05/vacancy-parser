package com.example.vacancyparser.api.dto;

import com.example.vacancyparser.domain.VacancyStatus;

import java.time.Instant;
import java.util.Set;

public record VacancySummary(
        Long id,
        String title,
        String companyName,
        String location,
        String description,
        String jobUrl,
        Set<String> tags,
        String workMode,
        VacancyStatus status,
        Instant postedAt,
        Instant updatedAt
) {
}
