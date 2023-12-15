package com.followinsider.secapi.forms.refs.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LatestFeed {

    private String title;

    private String id;

    private Author author;

    private String updated;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LinkValue> link;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LatestFeedEntry> entry;

    @Getter
    @Setter
    public static class Author {

        private String name;

        private String email;

    }

}