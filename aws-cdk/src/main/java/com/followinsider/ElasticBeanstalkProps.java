package com.followinsider;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ElasticBeanstalkProps {

    private final String account;

    private final String region;

    private final String assetPath;

    private final String stackName;

    private final String appName;

    private final String certificateArn;

    private final String healthCheckPath;

    private final Map<String, String> vars;

}
