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
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BinanceProducer {

    @Autowired
    private KafkaTemplate<String, List<Ticker>> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "topic")
    private String topic;

    @Autowired
    private ExchangeService exchangeService;

    @Scheduled(fixedRate = 2000)
    public void loadData() {
        kafkaTemplate.send(topic, getTickers());
    }

    private List<Ticker> getTickers() {

        String url = exchangeService.getExchangeUrl(Exchange.BINANCE);
        List<Map<String, String>> tickers = restTemplate.getForObject(url + "/v3/ticker/price", List.class);
        return tickers.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Ticker convert(Map<String, String> info) {
        Ticker ticker = new Ticker();
        ticker.setBuyPrice(PriceFormat.format(info.get("price")));
        ticker.setExchange(Exchange.BINANCE);
        ticker.setPair(format(info.get("symbol")));
        return ticker;
    }

    private String format(String pair) {
        return pair.substring(0, 3) + "_" + pair.substring(3);
    }
}
