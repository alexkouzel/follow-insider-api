package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.net.http.HttpClient;

@Builder
@Getter
public class DataClientProps {

    private final RateLimiter rateLimiter;

    private final ObjectMapper jsonMapper;

    private final ObjectMapper xmlMapper;

    private final int maxRetries;

}
