package com.followinsider.core.trading.quarter;

import com.followinsider.common.SyncStatus;
import com.followinsider.core.trading.quarter.entities.Quarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuarterRepository extends JpaRepository<Quarter, Integer> {

    List<Quarter> findBySyncStatus(SyncStatus syncStatus);

    Optional<Quarter> findByYearAndQuarter(int year, int quarter);

}
