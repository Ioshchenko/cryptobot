package com.cryptobot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class User {
    private long id;
    private long telegramId;
    private Map<String, ExchangeKey> keys;

}
