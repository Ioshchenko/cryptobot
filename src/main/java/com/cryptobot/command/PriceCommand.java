package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;

public class PriceCommand implements Command {
    @Override
    public String buildMessage(CommandParameters parameters) {
        return "Please input trading pair, for example, BTC_USD";
    }
}
