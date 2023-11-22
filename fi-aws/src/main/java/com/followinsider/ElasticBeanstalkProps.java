package com.followinsider;

import lombok.Builder;
import lombok.Data;
import software.amazon.awscdk.StackProps;

@Data
@Builder
public class ElasticBeanstalkProps {

    private final StackProps stackProps;

    private final String instanceType;

    private final String healthUrl;

    private final String assetPath;

    private final String stackName;

    private final String appName;

    private final String alias;

    private final String port;

    private final boolean https;

}
