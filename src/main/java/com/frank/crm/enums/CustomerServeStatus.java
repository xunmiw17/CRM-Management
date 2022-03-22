package com.frank.crm.enums;

public enum CustomerServeStatus {
    CREATED("fw_001"),
    ASSIGNED("fw_002"),
    PROCED("fw_003"),
    FEED_BACK("fw_004"),
    ARCHIVED("fw_005");

    private String state;

    CustomerServeStatus(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
