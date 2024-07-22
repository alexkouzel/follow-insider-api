package com.followinsider.config;

import com.alexkouzel.client.EdgarClient;
import com.alexkouzel.filing.reference.FilingReferenceLoader;
import com.alexkouzel.filing.type.f345.OwnershipDocumentLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EdgarConfig {

    @Value("${sec.edgar_user_agent}")
    private String userAgent;

    @Bean
    public EdgarClient edgarClient() {
        return new EdgarClient(userAgent, (statusCode) -> {
            log.info("[EDGAR] Status code: {}", statusCode);
        });
    }

    @Bean
    public OwnershipDocumentLoader ownershipDocumentLoader(EdgarClient edgarClient) {
        return new OwnershipDocumentLoader(edgarClient);
    }

    @Bean
    public FilingReferenceLoader filingMetadataLoader(EdgarClient edgarClient) {
        return new FilingReferenceLoader(edgarClient);
    }

}
