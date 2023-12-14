package com.followinsider.forms.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.forms.des.BooleanDeserializer;
import lombok.*;

@Getter
@Setter
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