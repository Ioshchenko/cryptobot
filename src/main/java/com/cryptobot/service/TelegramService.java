package com.cryptobot.service;

import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.model.telegram.ResponseMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class TelegramService {

    private static final String URL = "https://api.telegram.org/bot" + System.getenv("TELEGRAM_TOKEN");
    @Autowired
    private RestTemplate restTemplate;

    public void sendMessage(RequestMessage message) {
        try {
            restTemplate.postForObject(URL + "/sendMessage", message, ResponseMessage.class);
        } catch (RestClientException e) {
            log.error(e);
        }
    }

}
