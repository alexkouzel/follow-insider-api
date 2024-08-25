package com.followinsider.modules.trading.form.loader;

public interface ScopedFormLoader {

    void loadLatest();

    void loadLastDays(int days);

    void loadDaysAgo(int daysAgo);

    void loadByCompany(int cik);

    void loadFiscalQuarterRange(String from, String to);

    void loadFiscalQuarter(int year, int quarter);

}
