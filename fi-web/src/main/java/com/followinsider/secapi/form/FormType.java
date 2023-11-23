package com.followinsider.secapi.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum FormType {

    F3("3"),        // initial insider transactions
    F4("4"),        // insider transactions
    F5("5"),        // annual insider transactions that weren't reported in form 4

    Q10("10-Q"),    // quarterly financial reports
    K10("10-K"),    // annual financial reports
    K8("8-K");      // events important to shareholders

    private final String value;

    private static final Map<String, FormType> VALUE_MAP = new HashMap<>();

    static {
        for (FormType type : values()) {
            VALUE_MAP.put(type.value, type);
        }
    }

    public static FormType ofValue(String value) {
        return VALUE_MAP.get(value);
    }

}