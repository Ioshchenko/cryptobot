package com.cryptobot.service;

import com.cryptobot.model.ExchangeConfig;
import lombok.Builder;

import java.util.Collections;
import java.util.Map;

@Builder
public class ExchangeService {
    private Map<String, ExchangeConfig> exchanges;

    public Map<String, ExchangeConfig> getExchanges() {
        return Collections.unmodifiableMap(exchanges);
    }

    public String getExchangeUrl(String exchange) {
        return getExchangeByName(exchange).getUrl();
    }

    private ExchangeConfig getExchangeByName(String name) {
        return exchanges.get(name);
    }


}
