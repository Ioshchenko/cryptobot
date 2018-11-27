package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;

public class ErrorCommand implements Command {
    @Override
    public String buildMessage(CommandParameters parameters) {
        return "Unfortunately, we not support this command";
    }
}
