package com.cryptobot.model.exmo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserInfo {
    private Map<String, Double> balances;
    private Map<String, Double> reserved;
}
