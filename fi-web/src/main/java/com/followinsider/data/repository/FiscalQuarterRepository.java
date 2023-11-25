package com.followinsider.data.repository;

import com.followinsider.data.entity.FiscalQuarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Integer> {

    @Query("SELECT q FROM FiscalQuarter q WHERE NOT q.fullyLoaded ORDER BY q.year, q.quarter DESC")
    List<FiscalQuarter> findUnloadedAndOrder();

    Optional<FiscalQuarter> findByYearAndQuarter(int year, int quarter);

}
