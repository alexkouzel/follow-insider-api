package com.followinsider.secapi.form.ref;

import com.followinsider.secapi.form.FormType;
import lombok.Data;

import java.util.Date;

@Data
public class FormRef {

    private final String accNo;

    private final String issuerCik;

    private final FormType type;

    private final Date filedAt;

}
