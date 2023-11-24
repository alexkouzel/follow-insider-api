package com.followinsider.parser.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.parser.des.BooleanDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relationship {

    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean isDirector;

    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean isOfficer;

    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean isTenPercentOwner;

    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean isOther;

    private String officerTitle;

    private String otherText;

}