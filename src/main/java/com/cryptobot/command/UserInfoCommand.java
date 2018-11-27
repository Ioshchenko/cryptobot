package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.exmo.UserInfo;
import com.cryptobot.service.exmo.ExmoService;
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
public class UserInfoCommand implements Command {
    private Configuration freemarkerConfig;
    private ExmoService exmoService;

    @Override
    public String buildMessage(CommandParameters parameters) {
        try {
            Template template = freemarkerConfig.getTemplate("user_info.ftl");
            Map<String, Object> data = new HashMap<>();
            Map<String, ExchangeKey> keys = parameters.getUser().getKeys();
            if (keys.containsKey(Exchange.EXMO)) {
                data.put("user", exmoService.getUserInfo(parameters.getUser()));
            } else {
                data.put("error", "error");
            }

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (TemplateException | IOException e) {
            log.error(e);
        }
        return "Sorry, we have problem with loading data from EXMO :(";
    }
}
