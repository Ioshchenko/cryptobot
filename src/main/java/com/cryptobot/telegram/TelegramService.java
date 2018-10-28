package com.cryptobot.telegram;

import com.cryptobot.telegram.dto.Response;
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
        Response object = restTemplate.getForObject(URL + "/getUpdates", Response.class);
        log.info(object);
    }

}
