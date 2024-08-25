package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.company.models.CompanyDto;
import com.followinsider.modules.trading.insider.models.InsiderDto;
import com.followinsider.modules.trading.trade.models.TradeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record FormDto(

        String accNo,

        CompanyDto company,

        InsiderDto insider,

        Set<String> insiderTitles,

        LocalDate filedAt,

        String xmlUrl,

        List<TradeDto> trades

) {}
