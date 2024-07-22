package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.company.models.CompanyDto;
import com.followinsider.modules.trading.insider.models.InsiderDto;
import com.followinsider.modules.trading.trade.models.TradeDto;

import java.time.LocalDate;
import java.util.Set;

public interface FormDto {

    String getAccNo();

    Set<TradeDto> getTrades();

    CompanyDto getCompany();

    InsiderDto getInsider();

    Set<String> getInsiderTitles();

    LocalDate getFiledAt();

    String getXmlUrl();

}
