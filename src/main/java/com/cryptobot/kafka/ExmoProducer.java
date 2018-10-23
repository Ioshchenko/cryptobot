package com.cryptobot.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Log4j2
public class ExmoProducer {

    private static final String URL = "https://api.exmo.com/v1/ticker/";
    @Autowired
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @Resource(name = "topic")
    private String topic;


    @Scheduled(fixedRate = 1000)
    public void loadData() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Map<String, String>> tikers = restTemplate.getForObject(URL, Map.class);
        Map<String, String> btcUsd = tikers.get("BTC_USD");
        kafkaTemplate.send(topic, btcUsd);
    }

}
