package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

public class EdgarClient extends DataClient {

    private final String userAgent;

    public EdgarClient(String userAgent, DataClientProps props) {
        super(props);
        this.userAgent = userAgent;
    }

    public EdgarClient() {
        this("TestCompany test@gmail.com",
                DataClientProps.builder()
                        .rateLimiter(new RateLimiter(TimeUnit.SECONDS, 10))
                        .jsonMapper(new ObjectMapper().registerModule(new JavaTimeModule()))
                        .xmlMapper(new XmlMapper().registerModule(new JavaTimeModule()))
                        .maxRetries(3)
                        .build());
    }

    @Override
    protected HttpRequest buildRequest(String url, String contentType) {
        return HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .header("User-Agent", userAgent)
                .header("Content-Type", contentType + ";charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate")
                .build();
    }

    @Override
    protected InputStream extractStream(HttpResponse<InputStream> response) throws IOException {
        rateLimiter.resetBackoff();

        String encoding = response.headers().firstValue("Content-Encoding").orElse("");
        InputStream stream = response.body();

        return switch (encoding) {
            case "" -> stream;
            case "gzip" -> new GZIPInputStream(stream);
            case "deflate" -> new DeflaterInputStream(stream);
            default -> throw new UnsupportedEncodingException();
        };
    }

    @Override
    protected void handleError(HttpResponse<?> response) {
        switch (response.statusCode()) {
            case 301 -> {
                String location = response.headers().firstValue("Location").orElse(null);
                System.out.println("[EDGAR] Redirected to: " + location);
            }
            case 429 -> rateLimiter.applyDelaySeq(TimeUnit.MINUTES, 30, 60, 120);
            default -> rateLimiter.applyDelaySeq(TimeUnit.SECONDS, 20, 120, 600);
        }
    }

}
