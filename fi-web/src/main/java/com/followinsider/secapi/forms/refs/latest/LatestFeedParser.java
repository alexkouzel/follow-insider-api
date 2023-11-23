package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.forms.refs.FormRefParser;
import com.followinsider.utils.CollectionUtils;
import com.followinsider.utils.DateUtils;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestFeedParser implements FormRefParser<LatestFeed> {

    private static final Pattern ISSUER_CIK_PATTERN = Pattern.compile("data/(\\d+)");

    @Override
    public List<FormRef> parse(LatestFeed feed) throws ParseException {
        List<FormRef> refs = new ArrayList<>();
        for (LatestFeedEntry entry : feed.getEntry()) {
            refs.add(parseEntry(entry));
        }
        return refs;
    }

    private FormRef parseEntry(LatestFeedEntry entry) throws ParseException {
        String[] summaryParts = entry.getSummary().split(" ");

        // Parse accession number
        String accNo = summaryParts[4];

        // Parse issuer CIK
        String href = entry.getLink().getHref();
        Matcher matcher = ISSUER_CIK_PATTERN.matcher(href);
        String issueCik = matcher.find() ? matcher.group(1) : null;
        if (issueCik == null) {
            throw new ParseException("Couldn't match issuer CIK at href", -1);
        }

        // Parse form type
        String typeValue = entry.getCategory().getTerm();
        FormType type = FormType.ofValue(typeValue);

        // Parse filing date
        String filedAtValue = summaryParts[1];
        Date filedAt = DateUtils.tryParse(filedAtValue, "yyyy-MM-dd").orElse(null);

        return new FormRef(accNo, issueCik, type, filedAt);
    }

}
