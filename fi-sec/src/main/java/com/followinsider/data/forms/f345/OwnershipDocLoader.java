package com.followinsider.data.forms.f345;

import com.followinsider.client.DataClient;
import com.followinsider.client.EdgarClient;
import com.followinsider.data.forms.refs.FormRef;
import com.followinsider.utils.FormUtils;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OwnershipDocLoader {

    private final DataClient client;

    public OwnershipDocLoader() {
        this(new EdgarClient());
    }

    public OwnershipDoc loadByRef(FormRef ref) throws ParseException, IOException {
        String url = FormUtils.getTxtUrl(ref.issuerCik(), ref.accNum());
        return loadByUrl(url);
    }

    public OwnershipDoc loadByUrl(String url) throws IOException, ParseException {
        String txtData = client.loadText(url);
        return OwnershipDocParser.parse(txtData);
    }

}
