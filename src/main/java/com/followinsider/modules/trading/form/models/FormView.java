package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.company.models.CompanyView;
import com.followinsider.modules.trading.insider.models.InsiderView;
import com.followinsider.modules.trading.trade.models.TradeView;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Set;

public interface FormView {

    String getAccNo();

    Set<TradeView> getTrades();

    CompanyView getCompany();

    InsiderView getInsider();

    Set<String> getInsiderTitles();

    LocalDate getFiledAt();

    @Value("#{@formService.getXmlUrl(target)}")
    String getXmlUrl();

}
