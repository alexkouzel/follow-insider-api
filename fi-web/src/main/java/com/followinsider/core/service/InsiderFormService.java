package com.followinsider.core.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.core.util.InsiderFormParser;
import com.followinsider.core.entity.InsiderForm;
import com.followinsider.core.repository.InsiderFormRepository;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.util.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsiderFormService {

    private final EdgarClient edgarClient;

    private final InsiderFormRepository repository;

    public void loadByRefs(List<FormRef> refs) throws ParseException, IOException {
        if (refs == null) return;
        List<FormRef> newRefs = filterAlreadyLoaded(refs);

        // TODO: Save insiders.
        // TODO: Save Companies.

        for (List<FormRef> transactionRefs : CollectionUtil.divideBySize(newRefs, 2000)) {
            unsafeLoadByRefs(transactionRefs);
        }
    }

    private void unsafeLoadByRefs(List<FormRef> refs) throws ParseException, IOException {
        OwnershipDocLoader docLoader = new OwnershipDocLoader(edgarClient);
        List<InsiderForm> forms = new ArrayList<>();

        for (FormRef ref : refs) {
            OwnershipDoc doc = docLoader.loadByRef(ref);
            InsiderForm form = InsiderFormParser.parseOwnershipDoc(doc);
            forms.add(form);
        }
        repository.saveAll(forms);
    }

    private List<FormRef> filterAlreadyLoaded(List<FormRef> refs) {
        List<Date> dates = refs.stream().map(FormRef::getFiledAt).toList();

        Date date1 = Collections.min(dates);
        Date date2 = Collections.max(dates);

        Set<String> existingIds = repository.findIdsBetween(date1, date2);

        return refs.stream()
                .filter(ref -> !existingIds.contains(ref.getAccNum()))
                .collect(Collectors.toList());
    }

}
