package com.cryptobot.service;

import com.cryptobot.command.CommandContainer;
import com.cryptobot.model.CommandParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TelegramMessageService {

    @Autowired
    private CommandContainer commandContainer;

    public String buildTextMessage(CommandParameters command) {
        return commandContainer.execute(command);
    }

}
