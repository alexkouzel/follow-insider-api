package com.followinsider.loaders;

import com.followinsider.client.DataClient;
import com.followinsider.parsing.f345.OwnershipDoc;
import com.followinsider.parsing.f345.OwnershipDocParser;
import com.followinsider.parsing.refs.FormRef;
import com.followinsider.utils.FormUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OwnershipDocLoader {

    private final DataClient client;

    public List<OwnershipDoc> loadByRefs(List<FormRef> refs) throws ParseException, IOException {
        List<OwnershipDoc> docs = new ArrayList<>();
        for (FormRef ref : refs) {
            docs.add(loadByRef(ref));
        }
        return docs;
    }

    public OwnershipDoc loadByRef(FormRef ref) throws ParseException, IOException {
        String url = FormUtils.getTxtUrl(ref.getIssuerCik(), ref.getAccNum());
        return loadByUrl(url);
    }

    public OwnershipDoc loadByUrl(String url) throws IOException, ParseException {
        String txtData = client.loadText(url);
        return OwnershipDocParser.parse(txtData);
    }

}
