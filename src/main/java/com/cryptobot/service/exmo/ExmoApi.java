package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ExmoApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeService exchangeService;


    public Map<String, Map<String, String>> getTickers() {
        String url = exchangeService.getExchangeUrl(Exchange.EXMO);
        return restTemplate.getForObject(url + "/ticker/", Map.class);
    }

    public Map getOrderBook(List<String> pairs) {
        String url = exchangeService.getExchangeUrl(Exchange.EXMO);
        String uri = "/order_book/?limit=10&pair=" + String.join(",", pairs);
        return restTemplate.getForObject(url + uri, Map.class);
    }
}
