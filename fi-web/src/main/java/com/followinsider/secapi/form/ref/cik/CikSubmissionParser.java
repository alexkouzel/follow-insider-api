package com.followinsider.secapi.form.ref.cik;

import com.followinsider.secapi.form.FormType;
import com.followinsider.secapi.form.ref.FormRef;
import com.followinsider.secapi.form.ref.FormRefParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CikSubmissionParser implements FormRefParser<CikSubmission> {

    @Override
    public List<FormRef> parse(CikSubmission submission) throws ParseException {
        CikSubmissionFilings.Recent recent = submission.getFilings().getRecent();
        int formNum = recent.getAccessionNumber().size();

        List<FormRef> refs = new ArrayList<>();
        for (int i = 0; i < formNum; i++) {

            String accNo = recent.getAccessionNumber().get(i);
            String issuerCik = submission.getCik();
            FormType type = FormType.ofValue(recent.getForm().get(i));
            Date filedAt = recent.getFilingDate().get(i);

            FormRef ref = new FormRef(accNo, issuerCik, type, filedAt);
            refs.add(ref);
        }
        return refs;
    }

}
