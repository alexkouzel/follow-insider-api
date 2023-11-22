package com.followinsider;

import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;

import java.util.ArrayList;
import java.util.List;

public class CfnSettingsBuilder {

    private final List<CfnEnvironment.OptionSettingProperty> settings = new ArrayList<>();

    public List<CfnEnvironment.OptionSettingProperty> build() {
        return settings;
    }

    public CfnSettingsBuilder settingIf(boolean condition, String namespace, String optionName, String value) {
        return condition ? setting(namespace, optionName, value) : this;
    }

    public CfnSettingsBuilder setting(String namespace, String optionName, String value) {
        settings.add(CfnEnvironment.OptionSettingProperty.builder()
                .namespace(namespace)
                .optionName(optionName)
                .value(value)
                .build());

        return this;
    }

}
