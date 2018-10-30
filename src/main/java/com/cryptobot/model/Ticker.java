package com.cryptobot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ticker {
    private String exchange;
    private String pair;
    private String buyPrice;
}
