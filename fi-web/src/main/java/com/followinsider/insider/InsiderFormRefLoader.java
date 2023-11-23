package com.followinsider.insider;

import com.followinsider.secapi.client.DataClient;
import com.followinsider.secapi.forms.FormType;
import com.followinsider.secapi.forms.refs.FormRefLoader;

public class InsiderFormRefLoader extends FormRefLoader {

    public InsiderFormRefLoader(DataClient client) {
        super(client, FormType.F4);
    }

}
