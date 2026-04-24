package com.example.vacancyparser.mapper;

import com.example.vacancyparser.api.dto.VacancyDetails;
import com.example.vacancyparser.api.dto.VacancySummary;
import com.example.vacancyparser.domain.Vacancy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    VacancyDetails toDetails(Vacancy vacancy);

    VacancySummary toSummary(Vacancy vacancy);
}
