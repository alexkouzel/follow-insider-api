package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterDto;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterForms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalQuarterRepository extends JpaRepository<FiscalQuarter, Integer> {

    FiscalQuarter findByYearAndQuarter(int year, int quarter);

    @Query("SELECT fq FROM FiscalQuarter fq")
    List<FiscalQuarterDto> findAllDtos();

    FiscalQuarterDto findFirstByOrderByYearDescQuarterDesc();

    FiscalQuarterDto findFirstByOrderByYearAscQuarterAsc();

    @Query("SELECT COUNT(DISTINCT fq.year) FROM FiscalQuarter fq")
    long countYears();

}
