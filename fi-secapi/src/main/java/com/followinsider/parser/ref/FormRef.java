package com.followinsider.parser.ref;

import lombok.Data;

import java.util.Date;

@Data
public class FormRef {

    private final String accNum;

    private final String issuerCik;

    private final FormType type;

    private final Date filedAt;

}
