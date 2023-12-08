package com.followinsider.parsing.f345.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issuer {

    private String issuerCik;

    private String issuerName;

    private String issuerTradingSymbol;

}
