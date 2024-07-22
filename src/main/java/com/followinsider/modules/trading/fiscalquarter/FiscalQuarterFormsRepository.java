package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterForms;
import com.followinsider.modules.trading.form.loader.FormLoaderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalQuarterFormsRepository extends JpaRepository<FiscalQuarterForms, Integer> {

    @Query("SELECT fqf.fiscalQuarter FROM FiscalQuarterForms fqf WHERE fqf.loaderStatus = :loaderStatus")
    List<FiscalQuarter> findByLoaderStatus(FormLoaderStatus loaderStatus);

}
