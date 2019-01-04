package com.cryptobot.model.exmo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class OrderBook {
    private List<Bet> betsForPurchase;
    private List<Bet> betsForSale;
}
