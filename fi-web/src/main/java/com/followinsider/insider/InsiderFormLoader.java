package com.followinsider.insider;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.FormUtils;
import com.followinsider.secapi.forms.f345.OwnershipDocument;
import com.followinsider.secapi.forms.f345.OwnershipDocumentParser;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class InsiderFormLoader {

    private final DataClient client;

    public Optional<InsiderForm> loadFormByRef(FormRef ref) {
        if (ref == null) return Optional.empty();
        String url = FormUtils.getTxtUrl(ref.getIssuerCik(), ref.getAccNo());

        try {
            String txtData = client.loadText(url);
            OwnershipDocument doc = OwnershipDocumentParser.parseText(txtData);
            InsiderForm form = InsiderFormParser.parseOwnershipDocument(doc);
            return Optional.ofNullable(form);

        } catch (IOException | ParseException e) {
            log.error("Failed to load insider form: '{}'; url: '{}'", e.getMessage(), url);
            return Optional.empty();
        }
    }

}
