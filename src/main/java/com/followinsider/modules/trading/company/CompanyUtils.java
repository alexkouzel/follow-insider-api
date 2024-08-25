package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompanyUtils {

    public CompanyDto toDto(Company company) {
        return new CompanyDto(
                company.getCik(),
                company.getName(),
                company.getTicker(),
                company.getExchange()
        );
    }

}
