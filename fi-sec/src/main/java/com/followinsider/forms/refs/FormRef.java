package com.followinsider.forms.refs;

import com.followinsider.forms.FormType;

import java.util.Date;

public record FormRef(

        String accNum,

        String issuerCik,

        FormType type,

        Date filedAt

) {}
