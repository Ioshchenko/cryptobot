package com.cryptobot.service;

import com.cryptobot.model.Exchange;
import lombok.Builder;

import java.util.Collections;
import java.util.Map;

@Builder
public class ExchangeService {
    private Map<String, Exchange> exchanges;

    public Map<String, Exchange> getExchanges() {
        return Collections.unmodifiableMap(exchanges);
    }

    public String getExchangeUrl(String exchange) {
        return getExchangeByName(exchange).getUrl();
    }

    private Exchange getExchangeByName(String name) {
        return exchanges.get(name);
    }


}
