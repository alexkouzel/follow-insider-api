package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class LatestFeedLoader {

    private final DataClient client;

    private static final String LATEST_FEED_URL =
            "https://www.sec.gov/cgi-bin/browse-edgar" +
                    "?action=getcurrent" +
                    "&type=%s" +
                    "&start=%d" +
                    "&count=%d" +
                    "&output=atom";

    public List<FormRef> loadRefs(String formType, int start, int count) throws IOException {
        LatestFeed feed = loadFeed(formType, start, count);
        return LatestFeedParser.parseRefs(feed);
    }

    public LatestFeed loadFeed(String formType, int start, int count) throws IOException {
        String url = String.format(LATEST_FEED_URL, formType, start, count);
        return client.loadXmlType(url, LatestFeed.class);
    }

}
