package com.cryptobot.kafka;

import com.cryptobot.model.Ticker;
import com.cryptobot.service.TradingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
@Component
public class ExchangeConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TradingService tradingService;

    private ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "#{topic.toString()}")
    public void listen(List<LinkedHashMap<String, String>> tickers) {
        tradingService.update(mapper.convertValue(tickers, new TypeReference<List<Ticker>>() {
        }));
        template.convertAndSend("/topic/exchange", tickers);
    }
}
