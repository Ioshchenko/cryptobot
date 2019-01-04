package com.cryptobot.model.exmo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Bet {
    private double price;
    private double count;
    private double total;
}
