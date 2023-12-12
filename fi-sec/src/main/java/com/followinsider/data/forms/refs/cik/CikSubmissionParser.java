package com.followinsider.data.forms.refs.cik;

import com.followinsider.data.forms.refs.FormRef;
import com.followinsider.data.forms.refs.FormType;
import com.followinsider.utils.StringUtils;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UtilityClass
public class CikSubmissionParser {

    public List<FormRef> parse(CikSubmission submission) throws ParseException {
        CikSubmissionFilings.Recent recent = submission.getFilings().getRecent();
        int formNum = recent.getAccessionNumber().size();

        List<FormRef> refs = new ArrayList<>();
        for (int i = 0; i < formNum; i++) {

            String accNum = recent.getAccessionNumber().get(i);
            String issuerCik = StringUtils.pad(submission.getCik(), 10, '0');
            FormType type = FormType.ofValue(recent.getForm().get(i));
            Date filedAt = recent.getFilingDate().get(i);

            FormRef ref = new FormRef(accNum, issuerCik, type, filedAt);
            refs.add(ref);
        }
        return refs;
    }

}
