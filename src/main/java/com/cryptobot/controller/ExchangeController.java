package com.cryptobot.controller;

import com.cryptobot.model.Ticker;
import com.cryptobot.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExchangeController {
    @Autowired
    private TradingService tradingService;


    @GetMapping("/exchanges")
    public Map<String, Map<String, Ticker>> exchanges() {

        return tradingService.getPairs();
    }
}
