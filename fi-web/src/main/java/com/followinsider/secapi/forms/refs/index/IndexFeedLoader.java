package com.followinsider.secapi.forms.refs.index;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
public class IndexFeedLoader {

    private final DataClient client;

    public static final String FULL_INDEX_URL = "https://www.sec.gov/Archives/edgar/full-index/%d/QTR%d/master.idx";

    public List<FormRef> loadRefs(int year, int quarter) throws IOException {
        InputStream stream = loadStream(year, quarter);
        return IndexFeedParser.parseRefs(stream);
    }

    public IndexFeed loadFeed(int year, int quarter) throws IOException {
        InputStream stream = loadStream(year, quarter);
        return IndexFeedParser.parseFeed(stream);
    }

    public InputStream loadStream(int year, int quarter) throws IOException {
        String url = String.format(FULL_INDEX_URL, year, quarter);
        return client.loadStream(url, "text/html");
    }

}
