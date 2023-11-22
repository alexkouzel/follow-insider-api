package com.followinsider.secapi.forms.refs.submission;

import com.followinsider.secapi.forms.FormType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CikForm {

    private String accNo;

    private String act;

    private String fileNo;

    private String filmNo;

    private String items;

    private String primaryDoc;

    private String primaryDocDesc;

    private FormType type;

    private Date filedAt;

    private Date reportedAt;

    private Date acceptedAt;

    private boolean isXBRL;

    private boolean isInlineXBRL;

    private int size;

}
