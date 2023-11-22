package com.followinsider.secapi.forms.refs.submission;

import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.utils.CollectionUtils;

import java.util.List;

public class CikSubmissionParser {

    public static List<FormRef> parseRefs(CikSubmission submission) {
        List<CikForm> forms = submission.getFilings().getRecent().extractForms();
        return CollectionUtils.map(forms,
                form -> new FormRef(
                        form.getAccNo(),
                        submission.getCik(),
                        form.getType(),
                        form.getFiledAt()
                ));
    }

}
