package com.jlfang.demo.common.enums;

public enum DistanceMatrixStatusEnum {
    OK("OK"),
    INVALID_REQUEST("INVALID_REQUEST"),
    MAX_ELEMENTS_EXCEEDED("MAX_ELEMENTS_EXCEEDED"),
    MAX_DIMENSIONS_EXCEEDED("MAX_DIMENSIONS_EXCEEDED"),
    OVER_DAILY_LIMIT("OVER_DAILY_LIMIT"),
    OVER_QUERY_LIMIT("OVER_QUERY_LIMIT"),
    REQUEST_DENIED("REQUEST_DENIED"),
    UNKNOWN_ERROR("UNKNOWN_ERROR"),
    APPLICATION_INNER_ERROR("APPLICATION_INNER_ERROR");

    private final String value;

    DistanceMatrixStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DistanceMatrixStatusEnum fromValue(String value) {
        for (DistanceMatrixStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

