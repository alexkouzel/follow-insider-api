package com.followinsider.secapi.form.f345;

import lombok.Data;

import java.util.Date;

@Data
public class OwnershipDocument {

    private OwnershipForm ownershipForm;

    private String txtUrl;

    private String xmlUrl;

    private String accNo;

    private String submissionType;

    private Date reportedAt;

    private Date acceptedAt;

    private Date filedAt;

    private Date updatedAt;

    private int docCount;

}
