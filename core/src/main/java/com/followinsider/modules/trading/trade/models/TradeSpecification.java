package com.followinsider.modules.trading.trade.models;

import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.form.models.Form;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeSpecification implements Specification<Trade> {

    private final TradeFilters filters;

    public TradeSpecification(TradeFilters filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<Trade> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters == null) {
            return cb.conjunction();
        }

        Join<Trade, Form> formJoin = null;

        if (filters.companyCik() != null || filters.filedAt() != null) {
            formJoin = root.join("form");
        }

        if (filters.companyCik() != null) {
            predicates.add(cb.equal(formJoin.get("company").get("cik"), filters.companyCik()));
        }

        if (filters.executedAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("executedAt"), filters.executedAt()));
        }

        if (filters.filedAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(formJoin.get("filedAt"), filters.filedAt()));
        }

        if (filters.type() != null) {
            predicates.add(cb.equal(root.get("type"), filters.type()));
        }

        predicates.add(cb.lessThanOrEqualTo(root.get("executedAt"), LocalDate.now()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }

}