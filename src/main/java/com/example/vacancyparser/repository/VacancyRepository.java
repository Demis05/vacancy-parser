package com.example.vacancyparser.repository;

import com.example.vacancyparser.domain.Vacancy;
import com.example.vacancyparser.domain.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {

    Optional<Vacancy> findByExternalId(Long externalId);

    List<Vacancy> findAllByStatusAndExternalIdNotIn(VacancyStatus status, Collection<Long> externalIds);
}
