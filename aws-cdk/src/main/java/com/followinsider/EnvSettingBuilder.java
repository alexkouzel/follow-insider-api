package com.followinsider;

import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnvSettingBuilder {

    private final List<CfnEnvironment.OptionSettingProperty> settings = new ArrayList<>();

    public EnvSettingBuilder settingIf(boolean condition, String namespace, String optionName, String value) {
        return condition
                ? setting(namespace, optionName, value)
                : this;
    }

    public EnvSettingBuilder setting(String namespace, String optionName, String value) {
        settings.add(CfnEnvironment.OptionSettingProperty.builder()
                .namespace(namespace)
                .optionName(optionName)
                .value(value)
                .build());

        return this;
    }

    public EnvSettingBuilder vars(Map<String, String> vars) {
        vars.forEach((key, value) -> setting("aws:elasticbeanstalk:application:environment", key, value));
        return this;
    }

    public List<CfnEnvironment.OptionSettingProperty> build() {
        return settings;
    }

}
