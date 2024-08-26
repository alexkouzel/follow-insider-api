package com.followinsider;

import software.amazon.awscdk.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowInsiderApp {

    public static void main(final String[] args) {
        App app = new App();

        ElasticBeanstalkProps props = ElasticBeanstalkProps.builder()
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .region(System.getenv("CDK_DEFAULT_REGION"))
                .assetPath("../core/build/libs/core.jar")
                .stackName("64bit Amazon Linux 2 v3.7.5 running Corretto 17")
                .appName("follow-insider")
                .healthCheckPath("/actuator/health")
                .vars(buildVars())
                .build();

        new ElasticBeanstalkStack(app, props);

        // Emit the synthesized CloudFormation template
        app.synth();
    }

    private static Map<String, String> buildVars() {
        Map<String, String> vars = new HashMap<>();

        for (String name : buildVarNames()) {
            String value = System.getenv(name);

            if (value == null) {
                String error = String.format("'%s' variable is not present", name);
                throw new IllegalArgumentException(error);
            }

            vars.put(name, value);
        }
        return vars;
    }

    private static List<String> buildVarNames() {
        List<String> names = new ArrayList<>();

        // -- GENERAL --
        names.add("SERVER_PORT");
        names.add("EDGAR_USER_AGENT");

        // -- EMAIL --
        names.add("EMAIL_USERNAME");
        names.add("EMAIL_PASSWORD");
        names.add("EMAIL_SENDER");

        // -- ADMIN --
        names.add("PROD_ADMIN_USERNAME");
        names.add("PROD_ADMIN_PASSWORD");

        // -- DATABASE --
        names.add("PROD_DB_USERNAME");
        names.add("PROD_DB_PASSWORD");
        names.add("PROD_DB_URL");
        names.add("PROD_DB_DRIVER");

        return names;
    }

}
