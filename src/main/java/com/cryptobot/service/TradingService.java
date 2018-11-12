package com.cryptobot.service;

import com.cryptobot.model.Ticker;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class TradingService {

    private ConcurrentMap<String, ConcurrentMap<String, Ticker>> pairs = new ConcurrentHashMap<>();

    public void update(List<Ticker> tickers) {
        tickers.forEach(e -> {
            ConcurrentMap<String, Ticker> pairsOrDefault =
                    pairs.getOrDefault(e.getExchange(), new ConcurrentHashMap<>());
            pairsOrDefault.put(e.getPair(), e);
            pairs.putIfAbsent(e.getExchange(), pairsOrDefault);
        });
    }

    public List<Ticker> getTickersByPair(String pair) {
        return pairs.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(e -> e.containsKey(pair))
                .map(e -> e.get(pair))
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Ticker>> getPairs() {
        return Collections.unmodifiableMap(pairs);
    }

    public boolean containsPair(String pair) {
        return pairs.entrySet().stream()
                .map(Map.Entry::getValue)
                .anyMatch(e -> e.containsKey(pair));
    }
}
