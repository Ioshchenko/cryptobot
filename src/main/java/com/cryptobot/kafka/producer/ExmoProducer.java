package com.cryptobot.kafka.producer;

import com.cryptobot.kafka.dto.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

import static com.cryptobot.kafka.Exchange.Exmo.Pair.BTC_USD;
import static com.cryptobot.kafka.Exchange.Exmo.URL;

@Component
public class ExmoProducer {
    @Autowired
    private KafkaTemplate<String, Ticker> kafkaTemplate;

    @Resource(name = "topic")
    private String topic;

    @Scheduled(fixedRate = 1000)
    public void loadData() {
        Ticker ticker = getTicker(BTC_USD);
        kafkaTemplate.send(topic, ticker);
    }

    private Ticker getTicker(String pair) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Map<String, String>> tikers = restTemplate.getForObject(URL, Map.class);
        Ticker ticker = convert(tikers.get(pair));
        return ticker;
    }

    private Ticker convert(Map<String, String> info) {
        Ticker ticker = new Ticker();
        ticker.setBuyPrice(info.get("buy_price"));
        ticker.setExchange("exmo");
        ticker.setPair(Ticker.BTC_USD);
        return ticker;
    }

}
