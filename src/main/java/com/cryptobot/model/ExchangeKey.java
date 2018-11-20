package com.cryptobot.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangeKey {
    private String key;
    private String secret;
}
