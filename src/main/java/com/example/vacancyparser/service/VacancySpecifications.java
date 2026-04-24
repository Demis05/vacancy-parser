package com.example.vacancyparser.service;

import com.example.vacancyparser.service.model.VacancyFilter;
import com.example.vacancyparser.domain.Vacancy;
import com.example.vacancyparser.domain.VacancyStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class VacancySpecifications {

    private VacancySpecifications() {
    }

    public static Specification<Vacancy> byFilter(VacancyFilter filter) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), VacancyStatus.ACTIVE));

            if (hasText(filter.company())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("companyName")),
                        likePattern(filter.company())
                ));
            }

            if (hasText(filter.location())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        likePattern(filter.location())
                ));
            }

            if (hasText(filter.tag())) {
                Expression<String> tagExpression = root.join("tags").as(String.class);
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(tagExpression),
                        filter.tag().trim().toLowerCase()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String likePattern(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
