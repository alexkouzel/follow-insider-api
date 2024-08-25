package com.followinsider.modules.trading.form;

import com.alexkouzel.filing.FilingUrlBuilder;
import com.followinsider.modules.trading.company.CompanyUtils;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.form.models.FormDto;
import com.followinsider.modules.trading.insider.InsiderUtils;
import com.followinsider.modules.trading.trade.TradeUtils;
import com.followinsider.modules.trading.trade.models.TradeDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FormUtils {

    public FormDto toDto(Form form, boolean withTrades) {
        List<TradeDto> trades = null;

        if (withTrades) {
            trades = form
                    .getTrades().stream()
                    .map(trade -> TradeUtils.toDto(trade, false))
                    .toList();
        }
        return new FormDto(
                form.getAccNo(),
                CompanyUtils.toDto(form.getCompany()),
                InsiderUtils.toDto(form.getInsider()),
                form.getInsiderTitles(),
                form.getFiledAt(),
                FormUtils.getXmlUrl(form),
                trades
        );
    }

    private String getXmlUrl(Form form) {
        return FilingUrlBuilder.buildXmlUrl(
                form.getCompany().getCik(),
                form.getAccNo(),
                form.getXmlFilename());
    }

}
