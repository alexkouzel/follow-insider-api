package com.followinsider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.followinsider.client.EdgarClient;
import com.followinsider.loaders.FormRefLoader;
import com.followinsider.loaders.OwnershipDocLoader;
import com.followinsider.parsing.refs.FormType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppBeansConfig {

    @Value("${trading.edgar_user_agent}")
    private String userAgent;

    @Bean
    public EdgarClient edgarClient() {
        return new EdgarClient(userAgent);
    }

    @Bean
    public FormRefLoader formRefLoader(EdgarClient edgarClient) {
        return new FormRefLoader(edgarClient, FormType.F4);
    }

    @Bean
    public OwnershipDocLoader ownershipDocLoader(EdgarClient edgarClient) {
        return new OwnershipDocLoader(edgarClient);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
