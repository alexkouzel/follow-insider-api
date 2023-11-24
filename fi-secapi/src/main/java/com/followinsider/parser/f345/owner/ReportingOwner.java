package com.followinsider.parser.f345.owner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.parser.des.BooleanDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportingOwner {
    
    private ID reportingOwnerId;
    
    private Address reportingOwnerAddress;
    
    private Relationship reportingOwnerRelationship;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ID {
        
        private String rptOwnerCik;
        
        private String rptOwnerCcc;
        
        private String rptOwnerName;
        
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        
        private String rptOwnerStreet1;
        
        private String rptOwnerStreet2;
        
        private String rptOwnerCity;
        
        private String rptOwnerState;
        
        private String rptOwnerZipCode;
        
        private String rptOwnerStateDescription;

        @JsonDeserialize(using = BooleanDeserializer.class)
        private Boolean rptOwnerGoodAddress;
    
    }
    
}
