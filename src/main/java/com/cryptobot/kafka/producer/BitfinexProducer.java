package com.cryptobot.kafka.producer;

import com.cryptobot.kafka.dto.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.cryptobot.kafka.Exchange.Bitfinex.Pair.BTC_USD;
import static com.cryptobot.kafka.Exchange.Bitfinex.URL;

@Component
public class BitfinexProducer {
    @Autowired
    private KafkaTemplate<String, Ticker> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "topic")
    private String topic;


    @Scheduled(fixedRate = 6000)
    public void loadData() {
        Ticker ticker = getTicker(BTC_USD);
        kafkaTemplate.send(topic, ticker);
    }

    public Ticker getTicker(String pair) {
        List<List<String>> tikers = restTemplate.getForObject(URL, List.class);
        return convert(getTicker(pair, tikers));
    }

    private List getTicker(String pair, List<List<String>> tikers) {
        return tikers.stream()
                .filter(l -> l.contains(pair))
                .findFirst()
                .orElse(Collections.emptyList());
    }

    private Ticker convert(List info) {
        Ticker ticker = new Ticker();
        ticker.setPair(Ticker.BTC_USD);
        ticker.setExchange("bitfinex");
        if (!info.isEmpty()) {
            ticker.setBuyPrice(info.get(1).toString());
        }
        return ticker;
    }

}
