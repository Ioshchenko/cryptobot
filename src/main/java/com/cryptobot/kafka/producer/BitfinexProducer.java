package com.cryptobot.kafka.producer;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.Ticker;
import com.cryptobot.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BitfinexProducer {
    @Autowired
    private KafkaTemplate<String, List<Ticker>> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "topic")
    private String topic;

    @Autowired
    private ExchangeService exchangeService;


    @Scheduled(fixedRate = 6000)
    public void loadData() {
        kafkaTemplate.send(topic, getTickers());
    }

    private List<Ticker> getTickers() {
        String path = "/tickers?symbols=ALL";
        String url = exchangeService.getExchangeUrl(Exchange.BITFINEX) + path;
        List<List<String>> data = restTemplate.getForObject(url, List.class);
        return getExchangeTickers(data);
    }

    private List<Ticker> getExchangeTickers(List<List<String>> data) {
        return data.stream()
                .filter(i -> i.get(0).length() > 6)
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Ticker convert(List info) {
        Ticker ticker = new Ticker();
        ticker.setExchange(Exchange.BITFINEX);
        String pair = info.get(0).toString();
        ticker.setPair(format(pair));
        ticker.setBuyPrice(PriceFormat.format(info.get(1).toString()));

        return ticker;
    }

    private String format(String pair) {
        return pair.substring(1, 4) + "_" + pair.substring(4);
    }

}
