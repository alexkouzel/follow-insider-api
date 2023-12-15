package com.followinsider.core.trading.form.dtos;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface FormDto {

    String getAccNum();

    String getXmlUrl();

    LocalDate getFiledAt();

    @Value("#{target.company.cik}")
    String getCompanyCik();

    @Value("#{target.insider.cik}")
    String getInsiderCik();

}
