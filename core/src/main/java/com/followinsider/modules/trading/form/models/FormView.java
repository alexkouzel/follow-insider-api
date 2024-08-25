package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.company.models.CompanyDto;
import com.followinsider.modules.trading.insider.models.InsiderDto;
import com.followinsider.modules.trading.trade.models.TradeDto;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FormView {

    String getAccNo();

    CompanyDto getCompany();

    InsiderDto getInsider();

    Set<String> getInsiderTitles();

    LocalDate getFiledAt();

    @Value("#{FormUtils.getXmlUrl(target)}")
    String getXmlUrl();

    List<TradeDto> getTrades();

}
