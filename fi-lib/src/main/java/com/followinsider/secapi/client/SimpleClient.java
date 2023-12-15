package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class SimpleClient extends DataClient {

    public SimpleClient() {
        super(DataClientProps.builder()
                .rateLimiter(null)
                .jsonMapper(new ObjectMapper())
                .xmlMapper(new XmlMapper())
                .maxRetries(3)
                .build());
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
    protected void handleError(HttpResponse<?> response) {
        log.error("[HTTP] Error: {}", response.statusCode());
    }

}
