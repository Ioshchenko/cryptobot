package com.cryptobot.controller;

import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.model.telegram.UpdateEntity;
import com.cryptobot.service.TelegramService;
import com.cryptobot.service.TradingService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class TelegramController {
    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TradingService tradingService;

    @Resource(name = "freeMarkerConfiguration")
    private Configuration freemarkerConfig;

    @PostMapping("/telegram")
    public ResponseEntity update(@RequestBody UpdateEntity entity) {
        log.info(entity);

        RequestMessage message = new RequestMessage();
        message.setChatId(entity.getResponseMessage().getChat().getId());
        message.setReplyToMessageId(entity.getResponseMessage().getMessageId());
        message.setText(buildTextMessage());
        telegramService.sendMessage(message);

        return ResponseEntity.ok().build();
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
        return "Sorry, we have some problem :(";

    }
}
