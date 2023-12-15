package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.forms.FormType;

import java.time.LocalDate;
import java.util.Date;

public record FormRef(

        String accNum,

        String issuerCik,

        FormType type,

        LocalDate filedAt

) {}
