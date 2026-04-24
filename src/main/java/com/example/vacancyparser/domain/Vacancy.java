package com.example.vacancyparser.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(name = "job_slug", nullable = false, unique = true, length = 255)
    private String jobSlug;

    @Column(name = "company_slug", length = 255)
    private String companySlug;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_url", nullable = false, length = 1024)
    private String jobUrl;

    @Column(name = "source_apply_url", length = 1024)
    private String sourceApplyUrl;

    @Column(name = "location_name", length = 512)
    private String location;

    @Column(name = "work_mode", length = 64)
    private String workMode;

    @Column(name = "seniority", length = 128)
    private String seniority;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "salary_currency", length = 16)
    private String salaryCurrency;

    @Column(name = "salary_period", length = 32)
    private String salaryPeriod;

    @Column(name = "salary_min_cents")
    private Long salaryMinCents;

    @Column(name = "salary_max_cents")
    private Long salaryMaxCents;

    @Column(name = "equity_offered")
    private Boolean equityOffered;

    @Column(name = "posted_at")
    private Instant postedAt;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private VacancyStatus status = VacancyStatus.ACTIVE;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vacancy_tags", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(name = "tag", nullable = false)
    private Set<String> tags = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vacancy_skills", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Column(name = "skill", nullable = false)
    private Set<String> skills = new LinkedHashSet<>();

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (lastSeenAt == null) {
            lastSeenAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = new LinkedHashSet<>(Objects.requireNonNullElse(tags, Set.of()));
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = new LinkedHashSet<>(Objects.requireNonNullElse(skills, Set.of()));
    }
}
