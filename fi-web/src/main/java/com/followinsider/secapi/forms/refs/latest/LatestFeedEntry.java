package com.followinsider.secapi.forms.refs.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LatestFeedEntry {

    private String title;

    private String summary;

    private String id;

    private Category category;

    private LinkValue link;

    private Date updated;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category {

        @JacksonXmlProperty(isAttribute = true)
        private String scheme;

        @JacksonXmlProperty(isAttribute = true)
        private String label;

        @JacksonXmlProperty(isAttribute = true)
        private String term;

    }

}
