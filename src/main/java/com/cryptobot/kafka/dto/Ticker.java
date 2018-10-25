package com.cryptobot.kafka.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ticker {
    public static final String BTC_USD = "BTC_USD";
    private String exchange;
    private String pair;
    private String buyPrice;
}
