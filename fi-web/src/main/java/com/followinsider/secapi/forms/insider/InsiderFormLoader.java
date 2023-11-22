package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.FormUtils;
import com.followinsider.secapi.forms.f345.OwnershipDocument;
import com.followinsider.secapi.forms.f345.OwnershipDocumentParser;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@RequiredArgsConstructor
public class InsiderFormLoader {

    private final DataClient client;

    public Optional<InsiderForm> loadForm(FormRef ref) {
        if (ref == null) return Optional.empty();
        String url = FormUtils.getTxtUrl(ref.getIssueCik(), ref.getAccNo());

        try {
            String txtData = client.loadText(url);
            OwnershipDocument doc = OwnershipDocumentParser.parseText(txtData);
            InsiderForm form = InsiderFormParser.parseOwnershipDocument(doc);
            return Optional.ofNullable(form);

        } catch (IOException | ParseException e) {
            System.out.println("[ERROR] Failed to load an insider form: " + e.getMessage());
            System.out.println("DEBUG --> " + url);

            return Optional.empty();
        }
    }

}
