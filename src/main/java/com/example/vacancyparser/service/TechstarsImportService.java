package com.example.vacancyparser.service;

import com.example.vacancyparser.domain.Vacancy;
import com.example.vacancyparser.domain.VacancyStatus;
import com.example.vacancyparser.repository.VacancyRepository;
import com.example.vacancyparser.service.model.DetailResult;
import com.example.vacancyparser.service.model.ImportCounters;
import com.example.vacancyparser.service.model.ImportResult;
import com.example.vacancyparser.service.model.TechstarsJobSummary;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@AllArgsConstructor
public class TechstarsImportService {

    private static final Logger log = LoggerFactory.getLogger(TechstarsImportService.class);
    @Qualifier("detailFetchExecutor")
    private final ExecutorService detailFetchExecutor;
    private final VacancySourceClient vacancySourceClient;
    private final VacancyRepository vacancyRepository;

    @Transactional
    public ImportResult importVacancies() {
        List<TechstarsJobSummary> jobs = loadJobsOrThrow();
        Map<Long, String> jobDescriptionsById = fetchJobDescriptionsInParallel(jobs);
        Set<Long> importedJobIds = new HashSet<>();
        ImportCounters counters = saveOrUpdateVacancies(jobs, jobDescriptionsById, importedJobIds);
        int removedCount = removeMissingJobs(importedJobIds);
        ImportResult result = buildImportResult(jobs.size(), counters, removedCount);
        logImportResult(result);
        return result;
    }

    private List<TechstarsJobSummary> loadJobsOrThrow() {
        List<TechstarsJobSummary> jobs = vacancySourceClient.fetchJobs();
        if (jobs.isEmpty()) {
            throw new IllegalStateException("Techstars import returned zero vacancies. Aborting sync to avoid accidental deactivation.");
        }
        return jobs;
    }

    private Map<Long, String> fetchJobDescriptionsInParallel(List<TechstarsJobSummary> jobs) {
        List<TechstarsJobSummary> jobsWithDescription = jobs.stream()
                .filter(TechstarsJobSummary::hasDescription)
                .toList();
        if (jobsWithDescription.isEmpty()) {
            return Map.of();
        }
        List<CompletableFuture<DetailResult>> futures = jobsWithDescription.stream()
                .map(summary -> CompletableFuture.supplyAsync(
                        () -> new DetailResult(
                                summary.externalId(),
                                vacancySourceClient.fetchJobDetail(summary.companySlug(), summary.jobSlug())
                        ),
                        detailFetchExecutor
                ))
                .toList();

        Map<Long, String> jobDescriptionsById = new HashMap<>();
        for (CompletableFuture<DetailResult> future : futures) {
            DetailResult detailResult = future.join();
            jobDescriptionsById.put(detailResult.externalId(), detailResult.description());
        }
        return jobDescriptionsById;
    }

    private ImportCounters saveOrUpdateVacancies(
            List<TechstarsJobSummary> jobs,
            Map<Long, String> jobDescriptionsById,
            Set<Long> importedJobIds
    ) {
        int createdCount = 0;
        int updatedCount = 0;

        for (TechstarsJobSummary job : jobs) {
            importedJobIds.add(job.externalId());

            Vacancy vacancy = findOrCreateVacancy(job.externalId());
            boolean isNew = vacancy.getId() == null;

            applyJobSummary(vacancy, job);
            applyJobDescription(vacancy, jobDescriptionsById.get(job.externalId()));
            vacancyRepository.save(vacancy);

            if (isNew) {
                createdCount++;
            } else {
                updatedCount++;
            }
        }

        return new ImportCounters(createdCount, updatedCount);
    }

    private Vacancy findOrCreateVacancy(Long externalId) {
        return vacancyRepository.findByExternalId(externalId)
                .orElseGet(Vacancy::new);
    }

    private void applyJobDescription(Vacancy vacancy, String description) {
        if (description != null && !description.isBlank()) {
            vacancy.setDescription(description);
        }
    }

    private void applyJobSummary(Vacancy vacancy, TechstarsJobSummary summary) {
        buildSummary(vacancy, summary, Instant.now());
    }

    private int removeMissingJobs(Set<Long> importedJobIds) {
        List<Vacancy> activeVacancies = vacancyRepository.findAllByStatusAndExternalIdNotIn(VacancyStatus.ACTIVE, importedJobIds);
        vacancyRepository.deleteAll(activeVacancies);
        return activeVacancies.size();
    }

    private ImportResult buildImportResult(int retrievedCount, ImportCounters counters, int removedCount) {
        return new ImportResult(retrievedCount, counters.createdCount(), counters.updatedCount(), removedCount);
    }

    private void logImportResult(ImportResult result) {
        log.info("Techstars import finished. retrieved={}, created={}, updated={}, removed={}",
                result.retrieved(),
                result.created(),
                result.updated(),
                result.removed()
        );
    }

    private String normalize(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        return rawValue.replace('_', ' ').trim();
    }

    private void buildSummary(Vacancy vacancy, TechstarsJobSummary summary, Instant seenAt) {
        vacancy.setExternalId(summary.externalId());
        vacancy.setJobSlug(summary.jobSlug());
        vacancy.setCompanySlug(summary.companySlug());
        vacancy.setTitle(summary.title());
        vacancy.setCompanyName(summary.companyName());
        vacancy.setJobUrl(summary.detailPageUrl());
        vacancy.setSourceApplyUrl(summary.sourceApplyUrl());
        vacancy.setLocation(summary.location());
        vacancy.setWorkMode(summary.workMode());
        vacancy.setSeniority(normalize(summary.seniority()));
        vacancy.setSalaryCurrency(summary.salaryCurrency());
        vacancy.setSalaryPeriod(summary.salaryPeriod());
        vacancy.setSalaryMinCents(summary.salaryMinCents());
        vacancy.setSalaryMaxCents(summary.salaryMaxCents());
        vacancy.setEquityOffered(summary.equityOffered());
        vacancy.setPostedAt(summary.postedAt());
        vacancy.setTags(summary.tags());
        vacancy.setSkills(summary.skills());
        vacancy.setStatus(VacancyStatus.ACTIVE);
        vacancy.setLastSeenAt(seenAt);
    }
}
