package com.followinsider.parser.ref.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LatestFeedEntry {

    private String title;

    private String summary;

    private String id;

    private Category category;

    private LinkValue link;

    private Date updated;

    @Data
    public static class Category {

        @JacksonXmlProperty(isAttribute = true)
        private String scheme;

        @JacksonXmlProperty(isAttribute = true)
        private String label;

        @JacksonXmlProperty(isAttribute = true)
        private String term;

    }

}
