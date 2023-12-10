package com.followinsider.config;

import com.followinsider.client.EdgarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EdgarClientConfig {

    @Value("${trading.edgar_user_agent}")
    private String userAgent;

    @Bean
    public EdgarClient edgarClient() {
        return new EdgarClient(userAgent);
    }

}
