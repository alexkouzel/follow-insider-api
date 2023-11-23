package com.followinsider.secapi.form.ref.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LatestFeed {

    private String title;

    private String id;

    private Author author;

    private Date updated;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LinkValue> link;

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<LatestFeedEntry> entry;

    @Data
    public static class Author {

        private String name;

        private String email;

    }

}