package com.followinsider.modules.trading.trade.models;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeSpecification implements Specification<Trade> {

    private final TradeFiltersDto filters;

    public TradeSpecification(TradeFiltersDto filters) {
        this.filters = filters == null
                ? new TradeFiltersDto(null, null, null, null, null)
                : filters;
    }

    @Override
    public Predicate toPredicate(Root<Trade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        var company = root.join("form").join("company");

        if (filters.companyCik() != null) {
            predicates.add(cb.equal(company.get("cik"), filters.companyCik()));

        } else if (filters.companyName() != null) {
            String pattern = "%" + filters.companyName() + "%";
            predicates.add(cb.or(
                    cb.like(company.get("name"), pattern),
                    cb.like(company.get("ticker"), pattern)
            ));
        }

        if (filters.executedAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("executedAt"), filters.executedAt()));
        }

        if (filters.filedAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.join("form").get("filedAt"), filters.filedAt()));
        }

        if (filters.type() != null) {
            predicates.add(cb.equal(root.get("type"), filters.type()));
        }

        predicates.add(cb.lessThanOrEqualTo(root.get("executedAt"), LocalDate.now()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }

}
