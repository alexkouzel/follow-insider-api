package com.followinsider.modules.trading.form.loader;

public interface FormLoader {

    void loadLatest();

    void loadLastDays(int days);

    void loadByCik(String cik);

    void loadFiscalQuarterRange(String from, String to);

    void loadByLoaderStatus(FormLoaderStatus formLoaderStatus);

    void loadFiscalQuarter(int year, int quarter);

}
