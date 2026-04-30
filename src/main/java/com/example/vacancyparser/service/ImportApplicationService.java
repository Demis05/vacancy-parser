package com.example.vacancyparser.service;

import com.example.vacancyparser.api.dto.ImportResponse;
import com.example.vacancyparser.service.model.ImportResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImportApplicationService {

    private final TechstarsImportService techstarsImportService;

    public ImportResponse importVacancies() {
        ImportResult result = techstarsImportService.importVacancies();
        return new ImportResponse(
                result.retrieved(),
                result.created(),
                result.updated(),
                result.removed()
        );
    }
}
