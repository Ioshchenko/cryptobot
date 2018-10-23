package com.cryptobot.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class ExmoConsumer {

    @KafkaListener(topics = "#{topic.toString()}")
    public void listen(Map<String, String> value) {
        log.info(value);
    }
}
