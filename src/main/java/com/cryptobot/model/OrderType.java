package com.cryptobot.model;

public enum OrderType {
    BUY("buy"),
    SELL("sell");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
