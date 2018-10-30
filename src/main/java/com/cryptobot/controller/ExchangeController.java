package com.cryptobot.controller;

import com.cryptobot.service.ExchangeService;
import com.cryptobot.model.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchanges")
    public Map<String, Exchange> exchanges() {
        return exchangeService.getExchanges();
    }
}
