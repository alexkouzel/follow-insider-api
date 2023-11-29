package com.followinsider.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class DataClient {

    protected final RateLimiter rateLimiter;

    private final ObjectMapper jsonMapper;

    private final ObjectMapper xmlMapper;

    private final HttpClient client;

    private final int maxRetries;

    public DataClient(int maxRetries, RateLimiter rateLimiter) {
        this.client = HttpClient.newHttpClient();
        this.jsonMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
        this.rateLimiter = rateLimiter;
        this.maxRetries = maxRetries;
    }

    public DataClient(int maxRetries) {
        this(maxRetries, null);
    }

    protected abstract HttpRequest buildRequest(String url, String contentType);

    protected abstract InputStream extractStream(HttpResponse<InputStream> response) throws IOException;

    protected abstract void handleError(int statusCode);

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
        return attemptsLeft > 0
                ? executeRequest(request).or(() -> tryRequestStream(request, attemptsLeft - 1))
                : Optional.empty();
    }

    private Optional<InputStream> executeRequest(HttpRequest request) {
        try {
            if (rateLimiter != null) {
                rateLimiter.acquirePermit();
            }
            var responseType = HttpResponse.BodyHandlers.ofInputStream();
            HttpResponse<InputStream> response = client.send(request, responseType);
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                handleError(statusCode);
                return Optional.empty();
            }
            InputStream stream = extractStream(response);
            return Optional.ofNullable(stream);
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

}
