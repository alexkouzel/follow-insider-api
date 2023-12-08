package com.followinsider.config;

import com.followinsider.client.EdgarClient;
import com.followinsider.loaders.FormRefLoader;
import com.followinsider.loaders.OwnershipDocLoader;
import com.followinsider.parsing.refs.FormType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EdgarLoadersConfig {

    private final EdgarClient edgarClient;

    @Bean
    public FormRefLoader formRefLoader() {
        return new FormRefLoader(edgarClient, FormType.F4);
    }

    @Bean
    public OwnershipDocLoader ownershipDocLoader() {
        return new OwnershipDocLoader(edgarClient);
    }

}
