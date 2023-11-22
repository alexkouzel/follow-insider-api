package com.followinsider.secapi.forms.refs.submission;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.followinsider.secapi.common.deserializers.BooleanDeserializer;
import com.followinsider.secapi.forms.FormType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CikSubmissionFormData {
    
    private List<String> accessionNumber;
    
    private List<String> act;
    
    private List<String> form;
    
    private List<String> fileNumber;
    
    private List<String> filmNumber;
    
    private List<String> items;
    
    private List<String> primaryDocument;
    
    private List<String> primaryDocDescription;
    
    private List<Date> filingDate;
    
    private List<Date> reportDate;
    
    private List<Date> acceptanceDateTime;
    
    private List<Integer> size;

    @JsonDeserialize(contentUsing = BooleanDeserializer.class)
    private List<Boolean> isXBRL;

    @JsonDeserialize(contentUsing = BooleanDeserializer.class)
    private List<Boolean> isInlineXBRL;

    public List<CikForm> extractForms() {
        List<CikForm> forms = new ArrayList<>();
        int formNum = accessionNumber.size();
        for (int i = 0; i < formNum; i++) {
            forms.add(extractForm(i));
        }
        return forms;
    }

    private CikForm extractForm(int i) {
        return CikForm.builder()
                .type(FormType.ofValue(form.get(i)))
                .primaryDocDesc(primaryDocDescription.get(i))
                .acceptedAt(acceptanceDateTime.get(i))
                .primaryDoc(primaryDocument.get(i))
                .isInlineXBRL(isInlineXBRL.get(i))
                .reportedAt(reportDate.get(i))
                .accNo(accessionNumber.get(i))
                .filedAt(filingDate.get(i))
                .fileNo(fileNumber.get(i))
                .filmNo(filmNumber.get(i))
                .isXBRL(isXBRL.get(i))
                .items(items.get(i))
                .size(size.get(i))
                .act(act.get(i))
                .build();
    }
    
}
