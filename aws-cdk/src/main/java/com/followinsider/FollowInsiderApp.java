package com.followinsider;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class FollowInsiderApp {

    public static void main(final String[] args) {
        App app = new App();

        ElasticBeanstalkProps props = ElasticBeanstalkProps.builder()
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .region(System.getenv("CDK_DEFAULT_REGION"))
                .assetPath("../core/build/libs/core.jar")
                .stackName("64bit Amazon Linux 2 v3.7.5 running Corretto 17")
                .appName("follow-insider")
                .serverPort("5000")
                .healthCheckPath("/actuator/health")
                .build();

        new ElasticBeanstalkStack(app, props);

        // Emits the synthesized CloudFormation template
        app.synth();
    }

}
