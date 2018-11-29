package com.cryptobot.model.exmo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class UserInfo implements Serializable {
    private Map<String, Double> balances;
    private Map<String, Double> reserved;
}
