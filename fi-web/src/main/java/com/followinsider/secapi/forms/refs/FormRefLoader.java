package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.daily.DailyFeedParser;
import com.followinsider.secapi.forms.refs.index.IndexFeedParser;
import com.followinsider.secapi.forms.refs.latest.LatestFeed;
import com.followinsider.secapi.forms.refs.latest.LatestFeedParser;
import com.followinsider.secapi.forms.refs.cik.CikSubmission;
import com.followinsider.secapi.forms.refs.cik.CikSubmissionParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<FormRef> loadDaysAgo(int daysAgo) throws IOException, ParseException {
        String url = String.format(DAILY_FEED_URL, daysAgo, formType.getValue());
        String txt = client.loadText(url);
        List<FormRef> refs = new DailyFeedParser().parse(txt);
        return filterRefs(refs);
    }

    public List<FormRef> loadByQuarter(int year, int quarter) throws IOException, ParseException {
        String url = String.format(FULL_INDEX_URL, year, quarter);
        InputStream stream = client.loadStream(url, "text/html");
        List<FormRef> refs = new IndexFeedParser().parse(stream);
        return filterRefs(refs);
    }

    public List<FormRef> loadLatest(int start, int count) throws IOException, ParseException {
        String url = String.format(LATEST_FEED_URL, formType.getValue(), start, count);
        LatestFeed feed = client.loadXmlType(url, LatestFeed.class);
        List<FormRef> refs = new LatestFeedParser().parse(feed);
        return filterRefs(refs);
    }

    public List<FormRef> loadByCik(String cik) throws IOException, ParseException {
        String url = String.format(SUBMISSIONS_URL, cik);
        CikSubmission submission = client.loadJsonType(url, CikSubmission.class);
        List<FormRef> refs = new CikSubmissionParser().parse(submission);
        return filterRefs(refs);
    }

    private List<FormRef> filterRefs(List<FormRef> refs) {
        return filterUniqueRefs(refs).stream()
                .filter(form -> form.getType() == formType)
                .collect(Collectors.toList());
    }

    private List<FormRef> filterUniqueRefs(List<FormRef> refs) {
        List<FormRef> uniqueRefs = new ArrayList<>();
        Set<String> takenAccNo = new HashSet<>();
        for (FormRef ref : refs) {
            if (!takenAccNo.contains(ref.getAccNo())) {
                uniqueRefs.add(ref);
                takenAccNo.add(ref.getAccNo());
            }
        }
        return uniqueRefs;
    }

}
