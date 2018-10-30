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
public class BitfinexProducer {
    private static final String EXCHANGE = "bitfinex";
    @Autowired
    private KafkaTemplate<String, List<Ticker>> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "topic")
    private String topic;

    @Autowired
    private ExchangeService exchangeService;


    @Scheduled(fixedRate = 6000)
    public void loadData() {
        kafkaTemplate.send(topic, getTickers());
}

    private List<Ticker> getTickers() {
        String path = "/tickers?symbols=ALL";
        List<List<String>> data = restTemplate.getForObject(exchangeService.getExchangeUrl(EXCHANGE) + path, List.class);
        return getExchangeTickers(data);
    }

    private List<Ticker> getExchangeTickers(List<List<String>> data) {
        Map<String, String> pairs = exchangeService.getExchangePairs(EXCHANGE);

        return data.stream()
                .filter(l -> pairs.containsKey(l.get(0)))
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Ticker convert(List info) {
        Map<String, String> pairs = exchangeService.getExchangePairs(EXCHANGE);
        Ticker ticker = new Ticker();
        ticker.setExchange(EXCHANGE);
        ticker.setPair(pairs.get(info.get(0).toString()));
        ticker.setBuyPrice(info.get(1).toString());

        return ticker;
    }

}
