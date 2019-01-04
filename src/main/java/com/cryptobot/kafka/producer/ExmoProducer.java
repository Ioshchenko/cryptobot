package com.cryptobot.kafka.producer;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.Ticker;
import com.cryptobot.service.exmo.ExmoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExmoProducer {
    @Autowired
    private KafkaTemplate<String, List<Ticker>> kafkaTemplate;

    @Autowired
    private ExmoApi exmoApi;

    @Resource(name = "topic")
    private String topic;

    @Scheduled(fixedRate = 2000)
    public void loadData() {
        kafkaTemplate.send(topic, getTickers());
    }

    private List<Ticker> getTickers() {
        Map<String, Map<String, String>> tickers = exmoApi.getTickers();
        return tickers.entrySet().stream()
                .map(e -> convert(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private Ticker convert(String pair, Map<String, String> info) {
        Ticker ticker = new Ticker();
        ticker.setBuyPrice(PriceFormat.format(info.get("buy_price")));
        ticker.setExchange(Exchange.EXMO);
        ticker.setPair(pair);
        return ticker;
    }

}
