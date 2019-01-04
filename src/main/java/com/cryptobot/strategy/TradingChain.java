package com.cryptobot.strategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TradingChain {
    private Pair startPair;
    private Pair mediumPair;
    private Pair endPair;
}


@Getter
@Builder
class Pair {
    private String name;
    private double price;
    private double quantity;

    public Pair(String name) {
        this.name = name;
    }
}