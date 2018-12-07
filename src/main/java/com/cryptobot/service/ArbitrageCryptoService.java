package com.cryptobot.service;

import com.cryptobot.model.Ticker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArbitrageCryptoService {
    private static final double FEE = 0.002;
    private double balanceUSD = 100;


    public List<String> process(List<Ticker> tickers) {
        Map<String, Double> pair = convert(tickers);
        double btc = getBuyCount(pair.get("BTC_USD"), balanceUSD);

        return pair.entrySet().stream()
                .filter(e -> e.getKey().endsWith("_BTC"))
                .map(e -> {
                    double count = getBuyCount(pair.get(e.getKey()), btc);
                    String[] split = e.getKey().split("_");
                    Double price = pair.get(split[0] + "_USD");
                    if (price != null) {
                        double usd = price * count;
                        double result = usd - usd * FEE;
                        if (result > balanceUSD) {
                            return String.format("[%s: %f]", split[0], result);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    private Map<String, Double> convert(List<Ticker> tickers) {
        return tickers.stream()
                .collect(Collectors.toMap(Ticker::getPair, t -> Double.valueOf(t.getBuyPrice())));
    }

    private double getBuyCount(Double btcUsd, double balanceUSD) {
        double btcCount = balanceUSD / btcUsd;
        return btcCount - btcCount * FEE;
    }
}
