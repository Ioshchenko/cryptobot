package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.service.TradingService;
import com.cryptobot.service.UserService;
import com.cryptobot.service.exmo.ExmoService;
import freemarker.template.Configuration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Log4j2
@Component
public class CommandContainer {
    @Resource(name = "freeMarkerConfiguration")
    private Configuration freemarkerConfig;
    @Autowired
    private TradingService tradingService;
    @Autowired
    private ExmoService exmoService;
    @Autowired
    private UserService userService;

    private Map<Predicate<String>, Command> commands = new HashMap<>();

    @PostConstruct
    public void init() {
        commands.put(c -> c.equalsIgnoreCase("/price"), new PriceCommand());
        commands.put(c -> c.equalsIgnoreCase("/user_info"), new UserInfoCommand(freemarkerConfig, exmoService));
        commands.put(c -> tradingService.containsPair(c.toUpperCase()), new PairsCommand(freemarkerConfig, tradingService));
        commands.put(c -> c.startsWith("key"), new AddSecretKeyCommand(userService));
    }


    public String execute(CommandParameters parameters) {
        log.info("User input: " + parameters.getUserInput());
        Command command = commands.entrySet().stream()
                .filter(e -> e.getKey().test(parameters.getUserInput()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(new ErrorCommand());

        return command.buildMessage(parameters);
    }
}
