package com.cryptobot.kafka;

import com.cryptobot.kafka.dto.Ticker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ExchangeConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(topics = "#{topic.toString()}")
    public void listen(Ticker ticker) {
        template.convertAndSend("/topic/greetings", ticker);
    }
}
