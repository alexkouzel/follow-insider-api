package com.followinsider.secapi.forms.refs.latest;

import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.utils.CollectionUtils;
import com.followinsider.secapi.utils.DateUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatestFeedParser {

    private static final Pattern ISSUE_CIK_PATTERN = Pattern.compile("data/(\\d+)");

    public static List<FormRef> parseRefs(LatestFeed feed) {
        return CollectionUtils.map(feed.getEntry(), LatestFeedParser::parseFormRef);
    }

    private static FormRef parseFormRef(LatestFeedEntry entry) {
        LatestForm form = parseForm(entry);
        return new FormRef(
                form.getAccNo(),
                form.getIssueCik(),
                form.getType(),
                form.getFiledAt()
        );
    }

    private static LatestForm parseForm(LatestFeedEntry entry) {
        String[] summaryParts = entry.getSummary().split(" ");

        // Accession number & reporting entity
        String accNo = summaryParts[3];
        String reportingEntity = entry.getTitle();

        // Issuer CIK
        String href = entry.getLink().getHref();
        Matcher matcher = ISSUE_CIK_PATTERN.matcher(href);
        String issueCik = matcher.group(1);

        // Filing date
        String filedAtValue = summaryParts[1];
        Date filedAt = DateUtils.tryParse(filedAtValue, "yyyy-MM-dd").orElse(null);

        // Form type
        String typeValue = entry.getCategory().getTerm();
        FormType type = FormType.ofValue(typeValue);

        return new LatestForm(accNo, issueCik, reportingEntity, type, filedAt);
    }

}
