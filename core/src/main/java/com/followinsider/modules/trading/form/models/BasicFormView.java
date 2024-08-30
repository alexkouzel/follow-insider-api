package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.company.models.CompanyView;
import com.followinsider.modules.trading.insider.models.InsiderView;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Set;

public interface BasicFormView {

    String getAccNo();

    CompanyView getCompany();

    InsiderView getInsider();

    Set<String> getInsiderTitles();

    LocalDate getFiledAt();

    @Value("#{T(com.followinsider.modules.trading.form.FormUtils).getXmlUrl(target)}")
    String getXmlUrl();

}
