package com.followinsider.secapi.forms.refs;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.daily.DailyFeedLoader;
import com.followinsider.secapi.forms.refs.index.IndexFeedLoader;
import com.followinsider.secapi.forms.refs.latest.LatestFeedLoader;
import com.followinsider.secapi.forms.refs.submission.CikSubmissionLoader;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
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

    public List<FormRef> loadDaysAgo(int daysAgo) throws IOException, ParseException {
        DailyFeedLoader loader = new DailyFeedLoader(client);
        List<FormRef> refs = loader.loadRefs(formType.getValue(), daysAgo);
        return filterRefs(refs);
    }

    public List<FormRef> loadByQuarter(int year, int quarter) throws IOException {
        IndexFeedLoader loader = new IndexFeedLoader(client);
        List<FormRef> refs = loader.loadRefs(year, quarter);
        return filterRefs(refs);
    }

    public List<FormRef> loadLatest(int start, int count) throws IOException {
        LatestFeedLoader loader = new LatestFeedLoader(client);
        List<FormRef> refs = loader.loadRefs(formType.getValue(), start, count);
        return filterRefs(refs);
    }

    public List<FormRef> loadByCik(String cik) throws IOException {
        CikSubmissionLoader loader = new CikSubmissionLoader(client);
        List<FormRef> refs = loader.loadRefs(cik);
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
