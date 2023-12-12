package com.followinsider.data.forms.f345;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OwnershipDoc {

    private OwnershipForm ownershipForm;

    private String txtUrl;

    private String xmlUrl;

    private String accNum;

    private String submissionType;

    private Date reportedAt;

    private Date filedAt;

    private Date updatedAt;

    private int docCount;

}
