package com.example.vacancyparser.service;

import com.example.vacancyparser.service.model.TechstarsJobSummary;

import java.util.List;

public interface VacancySourceClient {

    List<TechstarsJobSummary> fetchJobs();

    String fetchJobDetail(String companySlug, String jobSlug);
}
