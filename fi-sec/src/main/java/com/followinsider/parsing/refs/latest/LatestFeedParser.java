package com.followinsider.parsing.refs.latest;

import com.followinsider.parsing.refs.FormType;
import com.followinsider.parsing.refs.FormRef;
import com.followinsider.utils.DateUtils;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestFeedParser {

    private static final Pattern ISSUER_CIK_PATTERN = Pattern.compile("data/(\\d+)");

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
        String accNum = summaryParts[4];

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
        String filedAtValue = summaryParts[2];
        Date filedAt = DateUtils.parse(filedAtValue, "yyyy-MM-dd");

        return new FormRef(accNum, issueCik, type, filedAt);
    }

}
