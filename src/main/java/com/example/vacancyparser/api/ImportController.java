package com.example.vacancyparser.api;

import com.example.vacancyparser.api.dto.ImportResponse;
import com.example.vacancyparser.service.ImportApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@AllArgsConstructor
@Tag(name = "Import", description = "Operations for manual vacancy import")
public class ImportController {

    private final ImportApplicationService importApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Run vacancy import", description = "Starts manual import from Techstars and synchronizes vacancies with the local database.")
    public ImportResponse importVacancies() {
        return importApplicationService.importVacancies();
    }
}
