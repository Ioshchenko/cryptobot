package com.cryptobot.kafka;

import com.cryptobot.model.Ticker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class ExchangeConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(topics = "#{topic.toString()}")
    public void listen(List<Ticker> tickers) {
        template.convertAndSend("/topic/exchange", tickers);
    }
}
