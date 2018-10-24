package com.cryptobot.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class ExmoConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(topics = "#{topic.toString()}")
    public void listen(Map<String, String> value) {
        template.convertAndSend("/topic/greetings", value);
    }
}
