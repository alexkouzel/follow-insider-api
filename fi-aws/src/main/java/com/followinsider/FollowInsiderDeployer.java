package com.followinsider;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class FollowInsiderDeployer {

    public static void main(final String[] args) {
        App app = new App();

        Environment env = Environment.builder()
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .region(System.getenv("CDK_DEFAULT_REGION"))
                .build();

        StackProps stackProps = StackProps.builder().env(env).build();

        new ElasticBeanstalkStack(app, ElasticBeanstalkProps.builder()
                .stackProps(stackProps)
                .instanceType("t2.micro")
                .assetPath("../fi-web/build/libs/fi-web.jar")
                .stackName("64bit Amazon Linux 2023 v4.1.1 running Corretto 17")
                .appName("follow-insider")
                .alias("fi")
                .port("5000")
                .https(true)
                .build());

        app.synth();
    }

}