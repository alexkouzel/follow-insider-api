package com.followinsider.config;

import com.alexkouzel.client.EdgarClient;
import com.alexkouzel.filing.reference.FilingReferenceLoader;
import com.alexkouzel.filing.type.f345.OwnershipDocumentLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class EdgarConfig {

    @Value("${sec.edgar_user_agent}")
    private String userAgent;

    @Bean
    public EdgarClient edgarClient() {
        return new EdgarClient(userAgent, statusCodeHandler());
    }

    @Bean
    public OwnershipDocumentLoader ownershipDocumentLoader(EdgarClient edgarClient) {
        return new OwnershipDocumentLoader(edgarClient);
    }

    @Bean
    public FilingReferenceLoader filingMetadataLoader(EdgarClient edgarClient) {
        return new FilingReferenceLoader(edgarClient);
    }

    private Consumer<Integer> statusCodeHandler() {
        return (statusCode) -> {
            if (statusCode != 200) {
                log.error("Failed loading EDGAR data :: status_code: {}", statusCode);
            }
        };
    }

}
