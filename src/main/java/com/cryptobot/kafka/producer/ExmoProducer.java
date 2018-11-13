package com.cryptobot.kafka.producer;

import com.cryptobot.service.ExchangeService;
import com.cryptobot.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExmoProducer {
    private static final String EXCHANGE = "exmo";
    @Autowired
    private KafkaTemplate<String, List<Ticker>> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "topic")
    private String topic;

    @Autowired
    private ExchangeService exchangeService;

    @Scheduled(fixedRate = 1000)
    public void loadData() {
        kafkaTemplate.send(topic, getTickers());
    }

    private List<Ticker> getTickers() {

        Map<String, Map<String, String>> tickers = restTemplate.getForObject(exchangeService.getExchangeUrl(EXCHANGE) + "/ticker/", Map.class);
        return tickers.entrySet().stream()
                .map(e -> convert(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private Ticker convert(String pair, Map<String, String> info) {
        Ticker ticker = new Ticker();
        ticker.setBuyPrice(PriceFormat.format(info.get("buy_price")));
        ticker.setExchange(EXCHANGE);
        ticker.setPair(pair);
        return ticker;
    }

}
