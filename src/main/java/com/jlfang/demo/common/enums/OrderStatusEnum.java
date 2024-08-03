package com.jlfang.demo.common.enums;

public enum OrderStatusEnum {
    UNASSIGNED("UNASSIGNED"),
    ASSIGNED("ASSIGNED");

    private final String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatusEnum fromValue(String value) {
        for (OrderStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
