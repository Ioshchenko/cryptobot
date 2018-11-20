package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;

public interface Command {
    String buildMessage(CommandParameters text);
}
