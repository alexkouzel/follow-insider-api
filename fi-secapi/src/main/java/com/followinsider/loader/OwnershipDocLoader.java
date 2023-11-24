package com.followinsider.loader;

import com.followinsider.client.DataClient;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.f345.OwnershipDocParser;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.util.FormUtil;
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
        String url = FormUtil.getTxtUrl(ref.getIssuerCik(), ref.getAccNum());
        return loadByUrl(url);
    }

    public OwnershipDoc loadByUrl(String url) throws IOException, ParseException {
        String txtData = client.loadText(url);
        return OwnershipDocParser.parse(txtData);
    }

}
