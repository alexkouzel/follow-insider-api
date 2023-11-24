package com.followinsider.core.repository;

import com.followinsider.core.entity.FiscalQuarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Integer> {

    @Query("SELECT q FROM FiscalQuarter q WHERE NOT q.isFullyLoaded ORDER BY q.year, q.quarter")
    List<FiscalQuarter> findNotLoadedAndSort();

}
