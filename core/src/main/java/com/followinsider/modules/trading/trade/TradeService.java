package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private final EntityManager entityManager;

    @Cacheable("trade")
    @Transactional(readOnly = true)
    public List<TradeView> getPage(TradePageRequest request) {
        Specification<Trade> spec = new TradeSpecification(request.tradeFilters());

        Sort sort = Sort.by(Sort.Direction.DESC, "executedAt");
        Pageable pageable = request.getPageRequest().prepare(sort);

        List<Integer> ids = findIdsByPage(pageable, spec);
        return tradeRepository.findByIds(ids);
    }

    @Cacheable("trade_count")
    @Transactional(readOnly = true)
    public long count(TradeFilters filters) {
        Specification<Trade> spec = new TradeSpecification(filters);
        return tradeRepository.count(spec);
    }

    private List<Integer> findIdsByPage(Pageable pageable, Specification<Trade> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Trade> root = query.from(Trade.class);

        // Select only the 'id' field
        query.select(root.get("id"));

        // Apply the Specification if provided
        Predicate predicate = spec.toPredicate(root, query, cb);
        query.where(predicate);

        // Apply sorting from pageable
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<Order> orders = sort.stream()
                .map(order -> order.isAscending()
                    ? cb.asc(root.get(order.getProperty()))
                    : cb.desc(root.get(order.getProperty())))
                .toList();

            query.orderBy(orders);
        }

        // Apply pagination
        TypedQuery<Integer> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Execute the query and return the result list
        return typedQuery.getResultList();
    }

}
