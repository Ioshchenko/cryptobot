package com.cryptobot.service;

import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.model.telegram.ResponseMessage;
import com.cryptobot.model.telegram.TelegramResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TelegramService {

    private static final String URL = "https://api.telegram.org/bot" + System.getenv("TELEGRAM_TOKEN");
    @Autowired
    private RestTemplate restTemplate;

    public void getUpdates() {
        TelegramResponse object = restTemplate.getForObject(URL + "/getUpdates", TelegramResponse.class);
        log.info(object);
    }

    public void sendMessage(RequestMessage message) {
        restTemplate.postForObject(URL + "/sendMessage", message, ResponseMessage.class);
    }

}
