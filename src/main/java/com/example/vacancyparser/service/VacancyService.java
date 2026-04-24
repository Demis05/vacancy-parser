package com.example.vacancyparser.service;

import com.example.vacancyparser.api.dto.VacancyDetails;
import com.example.vacancyparser.api.dto.VacancySummary;
import com.example.vacancyparser.exception.VacancyNotFoundException;
import com.example.vacancyparser.mapper.VacancyMapper;
import com.example.vacancyparser.repository.VacancyRepository;
import com.example.vacancyparser.service.model.VacancyFilter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final VacancyMapper vacancyMapper;

    public VacancyDetails getById(Long id) {
        return vacancyRepository.findById(id)
                .map(vacancyMapper::toDetails)
                .orElseThrow(() -> new VacancyNotFoundException(id));
    }

    public Page<VacancySummary> getVacancies(VacancyFilter filter, Pageable pageable) {
        return vacancyRepository.findAll(VacancySpecifications.byFilter(filter), pageable)
                .map(vacancyMapper::toSummary);
    }
}
