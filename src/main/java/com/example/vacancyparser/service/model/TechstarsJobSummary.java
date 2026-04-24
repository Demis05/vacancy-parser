package com.example.vacancyparser.service.model;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

public record TechstarsJobSummary(
        Long externalId,
        String jobSlug,
        String companySlug,
        String title,
        String companyName,
        String detailPageUrl,
        String sourceApplyUrl,
        String location,
        String workMode,
        String seniority,
        String salaryCurrency,
        String salaryPeriod,
        Long salaryMinCents,
        Long salaryMaxCents,
        Boolean equityOffered,
        Instant postedAt,
        Set<String> tags,
        Set<String> skills,
        boolean hasDescription
) {

    public TechstarsJobSummary {
        tags = tags == null ? new LinkedHashSet<>() : new LinkedHashSet<>(tags);
        skills = skills == null ? new LinkedHashSet<>() : new LinkedHashSet<>(skills);
    }
}
