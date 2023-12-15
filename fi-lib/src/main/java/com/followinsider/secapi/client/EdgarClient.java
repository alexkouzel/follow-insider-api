package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

@Slf4j
public class EdgarClient extends DataClient {

    private final String userAgent;

    public EdgarClient(String userAgent, DataClientProps props) {
        super(props);
        this.userAgent = userAgent;
    }

    public EdgarClient() {
        this("TestCompany test@gmail.com", defaultClientProps());
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
        if (response.statusCode() == 429) {
            log.error("[EDGAR] Too Many Requests :: Freezing client for 30/60/120 min");
            rateLimiter.applyDelaySequence(TimeUnit.MINUTES, 30, 60, 120);
        } else {
            log.error("[EDGAR] Unknown error :: {}",response.statusCode());
            rateLimiter.applyFixedDelay(TimeUnit.SECONDS, 1);
        }
    }

    private static DataClientProps defaultClientProps() {
        RateLimiter rateLimiter = new RateLimiter(TimeUnit.SECONDS, 10);

        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());

        return DataClientProps.builder()
                .rateLimiter(rateLimiter)
                .jsonMapper(jsonMapper)
                .xmlMapper(xmlMapper)
                .maxRetries(3)
                .build();
    }

}
