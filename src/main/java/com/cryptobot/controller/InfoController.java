package com.cryptobot.controller;


import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.service.ExchangeService;
import com.cryptobot.service.TelegramService;
import com.cryptobot.service.TradingService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
public class InfoController {

    @Autowired
    private TradingService tradingService;

    @Autowired
    private TelegramService telegramService;

    @Resource(name = "freeMarkerConfiguration")
    private Configuration freemarkerConfig;

    @GetMapping("/")
    public String info() {
        RequestMessage message = new RequestMessage();
        message.setChatId(455544464);
        message.setReplyToMessageId(8);
        message.setText("Hello");


        //service.sendMessage(message);

        return "index";
    }


    @GetMapping("/t")
    public String update() {

        RequestMessage message = new RequestMessage();
        message.setChatId(455544464);
        message.setReplyToMessageId(8);
        message.setText(buildTextMessage());
        message.setParseMode("HTML");
        telegramService.sendMessage(message);

        return "index";
    }

    private String buildTextMessage() {
        try {
            Template template = freemarkerConfig.getTemplate("pairs.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("pair", "BTC_USD");
            data.put("tickers", tradingService.getTickersByPair("BTC_USD"));
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
            return html;
        } catch (TemplateException | IOException e) {
            log.error(e);
        }
        return "Sorry";

    }


}
