package com.example.vacancyparser.service;

import com.example.vacancyparser.mapper.TechstarsPayloadMapper;
import com.example.vacancyparser.service.model.TechstarsJobSummary;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TechstarsClient implements VacancySourceClient {

    private static final Logger log = LoggerFactory.getLogger(TechstarsClient.class);

    private final TechstarsPageLoader techstarsPageLoader;
    private final TechstarsNextDataExtractor techstarsNextDataExtractor;
    private final TechstarsPayloadMapper techstarsPayloadMapper;

    @Override
    public List<TechstarsJobSummary> fetchJobs() {
        try {
            Document document = techstarsPageLoader.loadJobsPage();
            JsonNode root = techstarsNextDataExtractor.extract(document.html());
            JsonNode jobsNode = root.path("props")
                    .path("pageProps")
                    .path("initialState")
                    .path("jobs")
                    .path("found");

            return mapJobs(jobsNode);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to fetch Techstars jobs page", exception);
        }
    }

    @Override
    public String fetchJobDetail(String companySlug, String jobSlug) {
        if (companySlug == null || companySlug.isBlank() || jobSlug == null || jobSlug.isBlank()) {
            return null;
        }
        try {
            Document document = techstarsPageLoader.loadJobPage(companySlug, jobSlug);
            JsonNode root = techstarsNextDataExtractor.extract(document.html());
            JsonNode detailNode = root.path("props")
                    .path("pageProps")
                    .path("initialState")
                    .path("jobs")
                    .path("currentJob");

            sleepBetweenDetailRequests();
            return techstarsPayloadMapper.toJobDescription(detailNode);
        } catch (IOException exception) {
            log.warn("Failed to fetch detail for companySlug={} jobSlug={}", companySlug, jobSlug, exception);
            return null;
        }
    }

    private List<TechstarsJobSummary> mapJobs(JsonNode jobsNode) {
        List<TechstarsJobSummary> jobs = new ArrayList<>();
        for (JsonNode jobNode : jobsNode) {
            String companySlug = readText(jobNode.path("organization").path("slug"));
            String jobSlug = readText(jobNode.path("slug"));
            String detailPageUrl = techstarsPageLoader.buildJobUrl(companySlug, jobSlug);
            jobs.add(techstarsPayloadMapper.toJobSummary(jobNode, detailPageUrl));
        }
        return jobs;
    }

    private String readText(JsonNode valueNode) {
        if (valueNode == null || valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }
        String value = valueNode.asText();
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void sleepBetweenDetailRequests() {
        int delayMillis = techstarsPageLoader.getDetailDelayMillis();
        if (delayMillis <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
