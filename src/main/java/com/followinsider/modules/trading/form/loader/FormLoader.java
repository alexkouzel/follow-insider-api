package com.followinsider.modules.trading.form.loader;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterRange;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterVals;

import java.util.Set;

public interface FormLoader {

    Set<String> updateLatest(Set<String> accNos);

    void loadLatest();

    void loadLastDays(int days);

    void loadByCik(String cik);

    void loadFiscalQuarter(int year, int quarter);

    void loadFiscalQuarter(FiscalQuarterVals vals);

    void loadFiscalQuarter(FiscalQuarter fiscalQuarter);

    void loadFiscalQuarterRange(String from, String to);

    void loadFiscalQuarterRange(FiscalQuarterVals from, FiscalQuarterVals to);

    void loadFiscalQuarterRange(FiscalQuarterRange range);

    void loadByLoaderStatus(FormLoaderStatus formLoaderStatus);

}
