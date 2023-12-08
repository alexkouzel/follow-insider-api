package com.followinsider.core.trading.quarter;

import com.followinsider.core.trading.form.sync.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Integer> {

    List<FiscalQuarter> findBySyncStatus(SyncStatus syncStatus);

    Optional<FiscalQuarter> findByYearValAndQuarterVal(int year, int quarter);

}
