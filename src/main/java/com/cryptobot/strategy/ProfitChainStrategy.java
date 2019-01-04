package com.cryptobot.strategy;

import com.cryptobot.model.Ticker;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cryptobot.strategy.StrategyConfig.*;

public class ProfitChainStrategy implements Strategy<List<Ticker>, List<TradingChain>> {

    @Override
    public List<TradingChain> execute(List<Ticker> tickers) {
        Map<String, Double> pairs = convert(tickers);

        return buildTradingChains(pairs).stream()
                .map(t -> getProfitTradingChain(pairs, t))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private TradingChain getProfitTradingChain(Map<String, Double> pairs, TradingChain t) {
        Pair startPair = getPairForBudget(pairs, t.getStartPair());
        Pair mediumPair = getPairForCoins(pairs, t.getMediumPair().getName(), startPair.getQuantity());
        Pair endPair = getPairForCoins(pairs, t.getEndPair().getName(), mediumPair.getQuantity());

        if (isProfitable(endPair.getQuantity())) {
            return new TradingChain(startPair, mediumPair, endPair);
        }
        return null;
    }

    private Pair getPairForCoins(Map<String, Double> pairs, String name, double quantity) {
        double coins = getCountForCoins(quantity, pairs.get(name));
        return Pair.builder()
                .name(name)
                .price(pairs.get(name))
                .quantity(coins)
                .build();
    }

    private Pair getPairForBudget(Map<String, Double> pairs, Pair pair) {
        String pairName = pair.getName();
        double coinsForBudget = getCountForBudget(pairs.get(pairName), BALANCE_USD);
        return Pair.builder()
                .name(pairName)
                .price(pairs.get(pairName))
                .quantity(coinsForBudget)
                .build();

    }

    private List<TradingChain> buildTradingChains(Map<String, Double> pairs) {
        return pairs.entrySet().stream()
                .filter(e -> e.getKey().endsWith("USD"))
                .map(e -> buildChains(e.getKey(), pairs.keySet()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Map<String, Double> convert(List<Ticker> tickers) {
        return tickers.stream()
                .collect(Collectors.toMap(Ticker::getPair, t -> Double.valueOf(t.getBuyPrice())));
    }

    private List<TradingChain> buildChains(String startPair, Set<String> pairs) {
        String[] split = startPair.split("_");
        Set<String> mediumPairs = pairs.stream()
                .filter(p -> p.startsWith(split[0]))
                .collect(Collectors.toSet());

        return mediumPairs.stream()
                .filter(p -> pairs.contains(getPairWithUSD(p)) && !p.equals(startPair))
                .map(p -> new TradingChain(new Pair(startPair), new Pair(p), new Pair(getPairWithUSD(p))))
                .collect(Collectors.toList());
    }

    private String getPairWithUSD(String pair) {
        String[] values = pair.split("_");
        return values[1] + "_USD";
    }


    private boolean isProfitable(double result) {
        if (result > BALANCE_USD) {
            double profit = result - BALANCE_USD;
            return profit / BALANCE_USD > PROFIT;
        }
        return false;
    }

    private double getCountForBudget(double price, double balance) {
        double count = balance / price;
        return count - count * FEE;
    }

    private double getCountForCoins(double coinsForBudget, double price) {
        double coinsWithFee = price * coinsForBudget;
        return coinsWithFee - coinsWithFee * FEE;
    }

}
