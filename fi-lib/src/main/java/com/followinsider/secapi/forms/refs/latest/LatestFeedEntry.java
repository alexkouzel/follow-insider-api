package com.followinsider.secapi.forms.refs.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class LatestFeedEntry {

    private String title;

    private String summary;

    private String id;

    private Category category;

    private LinkValue link;

    private String updated;

    @Getter
    @Setter
    public static class Category {

        @JacksonXmlProperty(isAttribute = true)
        private String scheme;

        @JacksonXmlProperty(isAttribute = true)
        private String label;

        @JacksonXmlProperty(isAttribute = true)
        private String term;
    }

}
