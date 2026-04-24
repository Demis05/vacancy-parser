package com.example.vacancyparser.api.dto;

import com.example.vacancyparser.domain.VacancyStatus;

import java.time.Instant;
import java.util.Set;

public record VacancyDetails(
        Long id,
        String title,
        String companyName,
        String location,
        String description,
        String jobUrl,
        String sourceApplyUrl,
        Set<String> tags,
        Set<String> skills,
        String workMode,
        String seniority,
        String salaryCurrency,
        String salaryPeriod,
        Long salaryMinCents,
        Long salaryMaxCents,
        Boolean equityOffered,
        VacancyStatus status,
        Instant postedAt,
        Instant lastSeenAt,
        Instant createdAt,
        Instant updatedAt
) {
}
