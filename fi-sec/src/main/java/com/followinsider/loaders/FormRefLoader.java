package com.followinsider.loaders;

import com.followinsider.parsing.refs.FormRef;
import com.followinsider.parsing.refs.FormType;
import com.followinsider.parsing.refs.latest.LatestFeed;
import com.followinsider.parsing.refs.latest.LatestFeedParser;
import com.followinsider.client.DataClient;
import com.followinsider.parsing.refs.daily.DailyFeedParser;
import com.followinsider.parsing.refs.index.IndexFeedParser;
import com.followinsider.parsing.refs.cik.CikSubmission;
import com.followinsider.parsing.refs.cik.CikSubmissionParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@RequiredArgsConstructor
public class FormRefLoader {

    private final DataClient client;

    private final FormType formType;

    private static final String DAILY_FEED_URL = "https://www.sec.gov/cgi-bin/current?q1=%d&q3=%s";

    private static final String FULL_INDEX_URL = "https://www.sec.gov/Archives/edgar/full-index/%d/QTR%d/master.idx";

    private static final String SUBMISSIONS_URL = "https://data.sec.gov/submissions/CIK%s.json";

    private static final String LATEST_FEED_URL =
            "https://www.sec.gov/cgi-bin/browse-edgar" +
                    "?action=getcurrent" +
                    "&type=%s" +
                    "&start=%d" +
                    "&count=%d" +
                    "&output=atom";

    public List<FormRef> loadDaysAgo(int daysAgo) {
        try {
            String url = String.format(DAILY_FEED_URL, daysAgo, formType.getValue());
            String txt = client.loadText(url);
            List<FormRef> refs = new DailyFeedParser().parse(txt);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FormRef> loadByQuarter(int yearVal, int quarterVal) {
        try {
            String url = String.format(FULL_INDEX_URL, yearVal, quarterVal);
            InputStream stream = client.loadStream(url, "text/html");
            List<FormRef> refs = new IndexFeedParser().parse(stream);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FormRef> loadLatest(int start, int count) {
        try {
            String url = String.format(LATEST_FEED_URL, formType.getValue(), start, count);
            LatestFeed feed = client.loadXmlType(url, LatestFeed.class);
            List<FormRef> refs = new LatestFeedParser().parse(feed);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FormRef> loadByCik(String cik) {
        try {
            String url = String.format(SUBMISSIONS_URL, cik);
            CikSubmission submission = client.loadJsonType(url, CikSubmission.class);
            List<FormRef> refs = new CikSubmissionParser().parse(submission);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FormRef> filterRefs(List<FormRef> refs) {
        Map<String, FormRef> refMap = new HashMap<>();

        for (FormRef ref : refs) {
            refMap.put(ref.getAccNum(), ref);
        }
        return refMap.values().stream()
                .filter(form -> form.getType() == formType)
                .toList();
    }

}
