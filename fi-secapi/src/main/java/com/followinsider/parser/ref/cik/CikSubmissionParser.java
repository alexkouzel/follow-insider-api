package com.followinsider.parser.ref.cik;

import com.followinsider.parser.ref.FormRef;
import com.followinsider.parser.ref.FormType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CikSubmissionParser {

    public List<FormRef> parse(CikSubmission submission) throws ParseException {
        CikSubmissionFilings.Recent recent = submission.getFilings().getRecent();
        int formNum = recent.getAccessionNumber().size();

        List<FormRef> refs = new ArrayList<>();
        for (int i = 0; i < formNum; i++) {

            String accNum = recent.getAccessionNumber().get(i);
            String issuerCik = submission.getCik();
            FormType type = FormType.ofValue(recent.getForm().get(i));
            Date filedAt = recent.getFilingDate().get(i);

            FormRef ref = new FormRef(accNum, issuerCik, type, filedAt);
            refs.add(ref);
        }
        return refs;
    }

}
