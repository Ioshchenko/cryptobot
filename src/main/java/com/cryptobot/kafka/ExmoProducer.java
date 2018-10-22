package com.cryptobot.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

@Component
public class ExmoProducer {

    private static final String URL = "https://api.exmo.com/v1/ticker/";

    @Resource(name = "kafkaProperties")
    private Properties kafkaProperties;
    private Producer<String, String> producer;

    @PostConstruct
    private void init() {
        producer = new KafkaProducer<>(kafkaProperties);
    }

    @Scheduled(fixedRate = 1000)
    public void pullTiker() {
        RestTemplate restTemplate = new RestTemplate();
        producer.send(
                new ProducerRecord<>(
                        kafkaProperties.getProperty("topic"),
                        "exmo_ticker",
                        restTemplate.getForObject(URL, String.class)));
    }

}
