package com.followinsider.modules.trading.form.loader;

public interface FormLoader {

    void loadLatest();

    void loadLastDays(int days);

    void loadByCompany(int cik);

    void loadFiscalQuarterRange(String from, String to);

    void loadFiscalQuarter(int year, int quarter);

}
