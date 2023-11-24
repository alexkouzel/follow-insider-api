package com.followinsider.parser.f345;

import lombok.Data;

import java.util.Date;

@Data
public class OwnershipDoc {

    private OwnershipForm ownershipForm;

    private String txtUrl;

    private String xmlUrl;

    private String accNum;

    private String submissionType;

    private Date reportedAt;

    private Date acceptedAt;

    private Date filedAt;

    private Date updatedAt;

    private int docCount;

}
