package com.example.vacancyparser.service;

import com.example.vacancyparser.config.TechstarsProperties;
import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TechstarsPageLoader {

    private final TechstarsProperties properties;

    public Document loadJobsPage() throws IOException {
        return request(buildJobsUrl());
    }

    public Document loadJobPage(String companySlug, String jobSlug) throws IOException {
        return request(buildJobUrl(companySlug, jobSlug));
    }

    public String buildJobUrl(String companySlug, String jobSlug) {
        return properties.baseUrl() + "/companies/" + companySlug + "/jobs/" + jobSlug;
    }

    public int getDetailDelayMillis() {
        return properties.http().detailDelayMillis();
    }

    private Document request(String url) throws IOException {
        Connection connection = Jsoup.connect(url)
                .userAgent(properties.http().userAgent())
                .timeout(properties.http().timeoutMillis())
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Cache-Control", "no-cache")
                .followRedirects(true);

        return connection.get();
    }

    private String buildJobsUrl() {
        return properties.baseUrl() + properties.jobsPath();
    }

}
