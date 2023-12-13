package com.followinsider.data.forms.refs;

import com.followinsider.client.DataClient;
import com.followinsider.client.EdgarClient;
import com.followinsider.data.forms.refs.cik.CikSubmission;
import com.followinsider.data.forms.refs.cik.CikSubmissionParser;
import com.followinsider.data.forms.refs.daily.DailyFeedParser;
import com.followinsider.data.forms.refs.index.IndexFeedParser;
import com.followinsider.data.forms.refs.latest.LatestFeed;
import com.followinsider.data.forms.refs.latest.LatestFeedParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FormRefLoader {

    private final DataClient client;

    private final FormType formType;

    private static final String DAILY_FEED_URL = "https://www.sec.gov/cgi-bin/current?q1=%d&q3=%s";

    private static final String FULL_INDEX_URL = "https://www.sec.gov/Archives/edgar/full-index/%d/QTR%d/master.idx";

    private static final String SUBMISSIONS_URL = "https://data.sec.gov/submissions/CIK%s.json";

    // e.g. https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&type=4&start=0&count=200&output=atom
    private static final String LATEST_FEED_URL =
            "https://www.sec.gov/cgi-bin/browse-edgar" +
                    "?action=getcurrent" +
                    "&type=%s" +
                    "&start=%d" +
                    "&count=%d" +
                    "&output=atom";

    public FormRefLoader(FormType formType) {
        this(new EdgarClient(), formType);
    }

    public List<FormRef> loadDaysAgo(int daysAgo) {
        try {
            String url = String.format(DAILY_FEED_URL, daysAgo, formType.getValue());
            String txt = client.loadText(url);
            List<FormRef> refs = DailyFeedParser.parse(txt);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FormRef> loadByQuarter(int year, int quarter) {
        try {
            String url = String.format(FULL_INDEX_URL, year, quarter);
            InputStream stream = client.loadStream(url, "text/html");
            List<FormRef> refs = IndexFeedParser.parse(stream);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // possible 'count': 10, 20, 40, 80, 100
    public List<FormRef> loadLatest(int start, int count) {
        try {
            String url = String.format(LATEST_FEED_URL, formType.getValue(), start, count);
            LatestFeed feed = client.loadXmlType(url, LatestFeed.class);
            List<FormRef> refs = LatestFeedParser.parse(feed);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FormRef> loadByCik(String cik) {
        try {
            String url = String.format(SUBMISSIONS_URL, cik);
            CikSubmission submission = client.loadJsonType(url, CikSubmission.class);
            List<FormRef> refs = CikSubmissionParser.parse(submission);
            return filterRefs(refs);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FormRef> filterRefs(List<FormRef> refs) {
        return refs.stream()
                .collect(Collectors.toMap(FormRef::accNum, r -> r, (r1, r2) -> r1))
                .values().stream()
                .filter(form -> form.type() == formType)
                .collect(Collectors.toList());
    }

}
