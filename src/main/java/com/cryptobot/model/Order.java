package com.cryptobot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Order {
    private String pair;
    private Double quantity;
    private Double price;
    private OrderType type;
}
