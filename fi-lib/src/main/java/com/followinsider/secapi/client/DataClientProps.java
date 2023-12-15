package com.followinsider.secapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;

import java.net.http.HttpClient;

@Builder
@Getter
public class DataClientProps {

    private final RateLimiter rateLimiter;

    private final ObjectMapper jsonMapper;

    private final XmlMapper xmlMapper;

    private final int maxRetries;

}
