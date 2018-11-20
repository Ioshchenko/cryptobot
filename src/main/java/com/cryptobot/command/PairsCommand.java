package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.service.TradingService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@AllArgsConstructor
public class PairsCommand implements Command {
    private Configuration freemarkerConfig;
    private TradingService tradingService;

    @Override
    public String buildMessage(CommandParameters parameters) {
        try {
            Template template = freemarkerConfig.getTemplate("pairs.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("pair", parameters);
            data.put("tickers", tradingService.getTickersByPair(parameters.getUserInput()));
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (TemplateException | IOException e) {
            log.error(e);
        }
        return "Sorry, we have some problem :(";
    }
}
