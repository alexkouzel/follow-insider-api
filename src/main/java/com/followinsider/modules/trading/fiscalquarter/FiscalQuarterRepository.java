package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, String> {

    @Query("SELECT fq FROM FiscalQuarter fq")
    List<FiscalQuarterView> findAllViews();

    FiscalQuarterView findFirstByOrderByYearDescQuarterDesc();

    FiscalQuarterView findFirstByOrderByYearAscQuarterAsc();

    Optional<FiscalQuarter> findByYearAndQuarter(int year, int quarter);

    @Query("SELECT COUNT(DISTINCT fq.year) FROM FiscalQuarter fq")
    long countYears();

}
