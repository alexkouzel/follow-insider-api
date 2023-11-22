package com.followinsider.secapi.forms.refs.submission;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class CikSubmissionLoader {

    private final DataClient client;

    public static final String SUBMISSIONS_URL = "https://data.sec.gov/submissions/CIK%s.json";

    public List<FormRef> loadRefs(String cik) throws IOException {
        CikSubmission submission = loadSubmission(cik);
        return CikSubmissionParser.parseRefs(submission);
    }

    public CikSubmission loadSubmission(String cik) throws IOException {
        String url = String.format(SUBMISSIONS_URL, cik);
        return client.loadXmlType(url, CikSubmission.class);
    }

}
