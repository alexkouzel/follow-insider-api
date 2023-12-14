package com.followinsider.forms.f345;

import com.followinsider.client.DataClient;
import com.followinsider.client.EdgarClient;
import com.followinsider.forms.refs.FormRef;
import com.followinsider.forms.FormUrlParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
public class OwnershipDocLoader {

    private final DataClient client;

    public OwnershipDocLoader() {
        this(new EdgarClient());
    }

    public OwnershipDoc loadByRef(FormRef ref) throws ParseException, IOException {
        String url = FormUrlParser.getTxtUrl(ref);
        return loadByUrl(url);
    }

    public OwnershipDoc loadByUrl(String url) throws IOException, ParseException {
        String txtData = client.loadText(url);
        return OwnershipDocParser.parse(txtData);
    }

}
