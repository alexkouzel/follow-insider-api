package com.followinsider.insider;

import com.followinsider.secapi.forms.f345.owner.Issuer;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class InsiderForm {

    private String accNo;

    private Issuer issuer;

    private Insider insider;

    private List<InsiderTrade> trades;

    private Date reportedAt;

    private String txtUrl;

    private String xmlUrl;

}
