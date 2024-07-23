package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, String> {

    @Query("SELECT fq FROM FiscalQuarter fq")
    List<FiscalQuarterView> findAllViews();

    FiscalQuarter findByYearAndQuarter(int year, int quarter);

    FiscalQuarterView findFirstByOrderByYearDescQuarterDesc();

    FiscalQuarterView findFirstByOrderByYearAscQuarterAsc();

    @Query("SELECT COUNT(DISTINCT fq.year) FROM FiscalQuarter fq")
    long countYears();

}
