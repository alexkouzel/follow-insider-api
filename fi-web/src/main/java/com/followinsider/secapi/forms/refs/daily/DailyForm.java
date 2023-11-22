package com.followinsider.secapi.forms.refs.daily;

import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DailyForm {

    private String accNo;

    private String issuerCik;

    private String issuerName;

    private FormType type;

    private Date filedAt;

}
