package com.followinsider.client;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimpleClient extends DataClient {

    public SimpleClient() {
        super(3);
    }

    @Override
    protected HttpRequest buildRequest(String url, String contentType) {
        return HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", contentType + ";charset=UTF-8")
                .build();
    }

    @Override
    protected InputStream extractStream(HttpResponse<InputStream> response) {
        return response.body();
    }

    @Override
    protected void handleError(HttpResponse response) {
        System.out.println("[HTTP] Error: " + response.statusCode());
    }

}
