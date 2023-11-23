package com.followinsider.core.loader;

import com.followinsider.core.model.InsiderForm;
import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.form.FormType;
import com.followinsider.secapi.form.FormUtils;
import com.followinsider.secapi.form.f345.OwnershipDocument;
import com.followinsider.secapi.form.f345.OwnershipDocumentParser;
import com.followinsider.secapi.form.ref.FormRef;
import com.followinsider.secapi.form.ref.FormRefLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsiderFormLoader {

    private final DataClient client;

    private final InsiderFormSaver saver;

    public void loadDaysAgo(int daysAgo) throws IOException, ParseException {

        FormRefLoader refLoader = new FormRefLoader(client, FormType.F4);
        List<FormRef> refs = refLoader.loadDaysAgo(daysAgo);
        List<FormRef> savedRefs = List.of();
        refs = filterSavedRefs(refs, savedRefs);
        saveRefs(refs);

    }

    public void loadByQuarter(int year, int quarter) {
    }

    public void loadLatest(int start, int count) {
    }

    public void loadByCik(String cik) {
    }

    private void saveRefs(List<FormRef> refs) {
        List<InsiderForm> forms = new ArrayList<>();
        for (FormRef ref : refs) {
            Optional<InsiderForm> form = loadFormByRef(ref);
            form.ifPresent(forms::add);
        }
        saver.saveAll(forms);
    }

    private List<FormRef> filterSavedRefs(List<FormRef> refs, List<FormRef> savedRefs) {
        Set<String> accNos = savedRefs.stream()
                .map(FormRef::getAccNo)
                .collect(Collectors.toSet());

        return refs.stream()
                .filter(ref -> !accNos.contains(ref.getAccNo()))
                .toList();
    }

    private Optional<InsiderForm> loadFormByRef(FormRef ref) {
        if (ref == null) return Optional.empty();
        String url = FormUtils.getTxtUrl(ref.getIssuerCik(), ref.getAccNo());

        try {
            String txtData = client.loadText(url);
            OwnershipDocument doc = OwnershipDocumentParser.parseText(txtData);
            InsiderForm form = new InsiderFormParser().parseOwnershipDocument(doc);
            return Optional.ofNullable(form);

        } catch (IOException | ParseException e) {
            log.error("Failed to load insider form: '{}'; url: '{}'", e.getMessage(), url);
            return Optional.empty();
        }
    }

}
