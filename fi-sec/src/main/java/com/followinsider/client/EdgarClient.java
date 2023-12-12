package com.followinsider.client;

import com.followinsider.client.limiters.RateLimiter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

public class EdgarClient extends DataClient {

    private final String userAgent;

    public EdgarClient(String userAgent) {
        super(3, new RateLimiter(10));
        this.userAgent = userAgent;
    }

    public EdgarClient() {
        this("TestCompany test@gmail.com");
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
    protected void handleError(HttpResponse response) {
        int statusCode = response.statusCode();
        switch (response.statusCode()) {
            case 429 -> rateLimiter.handleTooManyRequests();
            case 301 -> {
                String location = response.headers().firstValue("Location").orElse(null);
                System.out.println("[EDGAR] Redirected to: " + location);
            }
            default -> rateLimiter.handleUnknownError(statusCode);
        }
    }

}
