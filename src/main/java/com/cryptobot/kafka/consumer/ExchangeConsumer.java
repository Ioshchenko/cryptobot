package com.cryptobot.kafka.consumer;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.Ticker;
import com.cryptobot.strategy.ArbitrageCryptoStrategy;
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

    @Autowired
    private ArbitrageCryptoStrategy arbitrageCryptoStrategy;

    private ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "#{topic.toString()}")
    public void receive(List<LinkedHashMap<String, String>> data) {
        List<Ticker> tickers
                = mapper.convertValue(data, new TypeReference<List<Ticker>>() {
        });
        Ticker ticker = tickers.get(0);
        if (ticker.getExchange().equals(Exchange.BITFINEX)) {
            arbitrageCryptoStrategy.start(tickers);
        }
        tradingService.update(tickers);
        template.convertAndSend("/topic/exchange", data);
    }
}
