package com.followinsider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.followinsider.secapi.client.DataClientProps;
import com.followinsider.secapi.client.EdgarClient;
import com.followinsider.secapi.client.RateLimiter;
import com.followinsider.secapi.forms.refs.FormRefLoader;
import com.followinsider.secapi.forms.f345.OwnershipDocLoader;
import com.followinsider.secapi.forms.FormType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppBeanConfig {

    @Value("${trading.edgar_user_agent}")
    private String userAgent;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Primary
    public ObjectMapper jsonMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        return jsonMapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        return xmlMapper;
    }

    @Bean
    public EdgarClient edgarClient(ObjectMapper jsonMapper, XmlMapper xmlMapper) {
        return new EdgarClient(userAgent,
                DataClientProps.builder()
                        .rateLimiter(new RateLimiter(TimeUnit.SECONDS, 10))
                        .jsonMapper(jsonMapper)
                        .xmlMapper(xmlMapper)
                        .maxRetries(3)
                        .build());
    }

    @Bean
    public FormRefLoader formRefLoader(EdgarClient edgarClient) {
        return new FormRefLoader(edgarClient, FormType.F4);
    }

    @Bean
    public OwnershipDocLoader ownershipDocLoader(EdgarClient edgarClient) {
        return new OwnershipDocLoader(edgarClient);
    }

}
