package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class DataClient {

    protected final RateLimiter rateLimiter;

    @Getter
    private final ObjectMapper jsonMapper;

    @Getter
    private final XmlMapper xmlMapper;

    private final int maxRetries;

    private final HttpClient client;

    public DataClient(DataClientProps props) {
        this.rateLimiter = props.getRateLimiter();
        this.jsonMapper = props.getJsonMapper();
        this.xmlMapper = props.getXmlMapper();
        this.maxRetries = props.getMaxRetries();
        this.client = HttpClient.newHttpClient();
    }

    protected abstract HttpRequest buildRequest(String url, String contentType);

    protected abstract InputStream extractStream(HttpResponse<InputStream> response) throws IOException;

    protected abstract void handleError(HttpResponse<?> response);

    public <T> T loadJsonType(String url, Class<T> type) throws IOException {
        return loadType(url, type, jsonMapper);
    }

    public <T> T loadXmlType(String url, Class<T> type) throws IOException {
        return loadType(url, type, xmlMapper);
    }

    public <T> T loadType(String url, Class<T> type, ObjectMapper mapper) throws IOException {
        return mapper.readValue(loadText(url), type);
    }

    public String loadText(String url) throws IOException {
        try (InputStream stream = loadStream(url, "text/html")) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public InputStream loadStream(String url, String contentType) throws IOException {
        HttpRequest request = buildRequest(url, contentType);
        return tryRequestStream(request, maxRetries)
                .orElseThrow(() -> new IOException("Request failed for all attempts"));
    }

    private Optional<InputStream> tryRequestStream(HttpRequest request, int attemptsLeft) {
        if (attemptsLeft <= 0) return Optional.empty();
        return executeRequest(request)
                .or(() -> tryRequestStream(request, attemptsLeft - 1));
    }

    private Optional<InputStream> executeRequest(HttpRequest request) {
        try {
            if (rateLimiter != null) {
                rateLimiter.acquirePermit();
            }
            var responseType = HttpResponse.BodyHandlers.ofInputStream();
            HttpResponse<InputStream> response = client.send(request, responseType);

            if (response.statusCode() != 200) {
                handleError(response);
                return Optional.empty();
            }
            InputStream stream = extractStream(response);
            return Optional.ofNullable(stream);

        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

}
