package com.cryptobot.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class TelegramMessageService {

    @Resource(name = "freeMarkerConfiguration")
    private Configuration freemarkerConfig;

    @Autowired
    private TradingService tradingService;


    public String buildTextMessage(String command) {
        if (command.equals("price")) {
            return "Please input trading pair, for example, BTC_USD";
        }
        if (!tradingService.contaisPair(command)) {
            return "Unfortunately, we not support this command";
        }
        return generateMessage(command);

    }

    private String generateMessage(String command) {
        try {
            Template template = freemarkerConfig.getTemplate("pairs.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("pair", command.toUpperCase());
            data.put("tickers", tradingService.getTickersByPair(command.toUpperCase()));
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (TemplateException | IOException e) {
            log.error(e);
        }
        return "Sorry, we have some problem :(";
    }
}
