package com.followinsider.secapi.forms.refs.daily;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
public class DailyFeedLoader {

    private final DataClient client;

    private static final String DAILY_FEED_URL = "https://www.sec.gov/cgi-bin/current?q1=%d&q3=%s";

    public List<FormRef> loadRefs(String formType, int daysAgo) throws IOException, ParseException {
        String data = loadData(formType, daysAgo);
        return DailyFeedParser.parseRefs(data);
    }

    public DailyFeed loadFeed(String formType, int daysAgo) throws IOException, ParseException {
        String data = loadData(formType, daysAgo);
        return DailyFeedParser.parseFeed(data);
    }

    public String loadData(String formType, int daysAgo) throws IOException {
        String url = String.format(DAILY_FEED_URL, daysAgo, formType);
        return client.loadText(url);
    }

}
