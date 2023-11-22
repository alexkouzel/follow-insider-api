package com.followinsider.secapi.forms.refs.latest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {

        private String name;

        private String email;

    }

}