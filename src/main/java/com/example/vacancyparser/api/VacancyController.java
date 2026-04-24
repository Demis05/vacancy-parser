package com.example.vacancyparser.api;

import com.example.vacancyparser.api.dto.VacancyDetails;
import com.example.vacancyparser.api.dto.VacancySummary;
import com.example.vacancyparser.service.model.VacancyFilter;
import com.example.vacancyparser.service.VacancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vacancies")
@AllArgsConstructor
@Tag(name = "Vacancies", description = "Operations for reading saved vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    @Operation(summary = "Get vacancies", description = "Returns saved vacancies with pagination and optional filters by company, location, and tag.")
    public Page<VacancySummary> getVacancies(
            @Parameter(description = "Filter by company name, case-insensitive contains match")
            @RequestParam(required = false) String company,
            @Parameter(description = "Filter by location, case-insensitive contains match")
            @RequestParam(required = false) String location,
            @Parameter(description = "Filter by tag, case-insensitive exact match")
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        VacancyFilter filter = new VacancyFilter(company, location, tag);
        return vacancyService.getVacancies(filter, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vacancy by id", description = "Returns one saved vacancy by internal database id.")
    public VacancyDetails getById(@PathVariable Long id) {
        return vacancyService.getById(id);
    }
}
