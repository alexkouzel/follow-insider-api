package com.followinsider.core.trading.form.download.auto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Integer> {

    @Query("SELECT q FROM FiscalQuarter q WHERE q.downloaded = false ORDER BY q.yearVal, q.quarterVal DESC")
    List<FiscalQuarter> findUnloadedAndOrder();

    Optional<FiscalQuarter> findByYearValAndQuarterVal(int year, int quarter);

}
