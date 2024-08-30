package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.Trade;
import com.followinsider.modules.trading.trade.models.TradeType;
import com.followinsider.modules.trading.trade.models.TradeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Query("SELECT t FROM Trade t " +
        "JOIN FETCH t.form f " +
        "JOIN FETCH f.company c " +
        "JOIN FETCH f.insider i " +
        "JOIN FETCH f.insiderTitles it " +
        "WHERE t.id IN (" +
        "   SELECT t0.id FROM Trade t0 " +
        "   WHERE (:companyCik IS NULL OR :companyCik = t0.form.company.cik) " +
        "   AND (:executedAt IS NULL OR :executedAt < t0.executedAt) " +
        "   AND (:filedAt IS NULL OR :filedAt < t0.form.filedAt) " +
        "   AND (:type IS NULL OR :type = t0.type) " +
        "   ORDER BY t0.executedAt " +
        "   LIMIT :limit OFFSET :offset" +
        ") " +
        "ORDER BY t.executedAt"
    )
    List<TradeView> findPage(
        int limit,
        int offset,
        String companyCik,
        LocalDate executedAt,
        LocalDate filedAt,
        TradeType type
    );

    @Query("SELECT COUNT(t) FROM Trade t " +
        "WHERE (:companyCik IS NULL OR :companyCik = t.form.company.cik) " +
        "AND (:executedAt IS NULL OR :executedAt < t.executedAt) " +
        "AND (:filedAt IS NULL OR :filedAt < t.form.filedAt) " +
        "AND (:type IS NULL OR :type = t.type)"
    )
    long count(
        String companyCik,
        LocalDate executedAt,
        LocalDate filedAt,
        TradeType type
    );

}
