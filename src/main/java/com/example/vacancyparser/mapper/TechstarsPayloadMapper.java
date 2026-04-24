package com.example.vacancyparser.mapper;

import com.example.vacancyparser.service.model.TechstarsJobSummary;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class TechstarsPayloadMapper {

    public TechstarsJobSummary toJobSummary(JsonNode jobNode, String detailPageUrl) {
        JsonNode organizationNode = jobNode.path("organization");

        return new TechstarsJobSummary(
                longOrNull(jobNode.path("id")),
                readText(jobNode.path("slug")),
                readText(organizationNode.path("slug")),
                readText(jobNode.path("title")),
                readText(organizationNode.path("name")),
                detailPageUrl,
                readText(jobNode.path("url")),
                readFirstText(jobNode.path("locations")),
                readText(jobNode.path("workMode")),
                readText(jobNode.path("seniority")),
                readText(jobNode.path("compensationCurrency")),
                readText(jobNode.path("compensationPeriod")),
                longOrNull(jobNode.path("compensationAmountMinCents")),
                longOrNull(jobNode.path("compensationAmountMaxCents")),
                booleanOrNull(jobNode.path("compensationOffersEquity")),
                epochSecondsToInstant(jobNode.path("createdAt")),
                toOrderedSet(organizationNode.path("industryTags")),
                toOrderedSet(jobNode.path("skills")),
                jobNode.path("hasDescription").asBoolean(false)
        );
    }

    public String toJobDescription(JsonNode detailNode) {
        String descriptionHtml = readText(detailNode.path("description"));
        if (descriptionHtml == null || descriptionHtml.isBlank()) {
            return null;
        }

        return Jsoup.parseBodyFragment(descriptionHtml).text();
    }

    private Set<String> toOrderedSet(JsonNode arrayNode) {
        Set<String> values = new LinkedHashSet<>();
        if (!arrayNode.isArray()) {
            return values;
        }
        for (JsonNode item : arrayNode) {
            String value = readText(item);
            if (value != null && !value.isBlank()) {
                values.add(value.trim());
            }
        }
        return values;
    }

    private String readFirstText(JsonNode arrayNode) {
        if (!arrayNode.isArray() || arrayNode.isEmpty()) {
            return null;
        }
        return readText(arrayNode.get(0));
    }

    private Instant epochSecondsToInstant(JsonNode valueNode) {
        Long seconds = longOrNull(valueNode);
        return seconds == null ? null : Instant.ofEpochSecond(seconds);
    }

    private Long longOrNull(JsonNode valueNode) {
        return valueNode.isNumber() ? valueNode.longValue() : null;
    }

    private Boolean booleanOrNull(JsonNode valueNode) {
        return valueNode.isBoolean() ? valueNode.booleanValue() : null;
    }

    private String readText(JsonNode valueNode) {
        if (valueNode == null || valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }
        String value = valueNode.asText();
        return value == null || value.isBlank() ? null : value.trim();
    }
}
