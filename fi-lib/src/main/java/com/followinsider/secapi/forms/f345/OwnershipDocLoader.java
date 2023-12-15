package com.followinsider.secapi.forms.f345;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.client.EdgarClient;
import com.followinsider.secapi.forms.FormUrlParser;
import com.followinsider.secapi.forms.refs.FormRef;

import java.io.IOException;
import java.text.ParseException;

public class OwnershipDocLoader {

    private final OwnershipDocParser parser;

    private final DataClient client;

    public OwnershipDocLoader(DataClient client) {
        this.parser = new OwnershipDocParser(client.getXmlMapper());
        this.client = client;
    }

    public OwnershipDocLoader() {
        this(new EdgarClient());
    }

    public OwnershipDoc loadByRef(FormRef ref) throws ParseException, IOException {
        String txtData = client.loadText(ref.getTxtUrl());
        return parser.parse(txtData, ref);
    }

}
