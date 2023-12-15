package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.FormUrlParser;

import java.time.LocalDate;
import java.util.Date;

public record FormRef(

        String accNum,

        String issuerCik,

        FormType type,

        LocalDate filedAt

) {

    public String getTxtUrl() {
        return FormUrlParser.getTxtUrl(issuerCik, accNum);
    }

}
