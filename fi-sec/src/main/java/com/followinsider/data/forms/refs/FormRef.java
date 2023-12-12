package com.followinsider.data.forms.refs;

import java.util.Date;

public record FormRef(

        String accNum,

        String issuerCik,

        FormType type,

        Date filedAt
) {}
