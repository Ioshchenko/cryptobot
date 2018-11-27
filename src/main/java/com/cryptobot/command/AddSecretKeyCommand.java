package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.User;
import com.cryptobot.service.UserService;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSecretKeyCommand implements Command {
    private static final Pattern KEY_PATTERN = Pattern.compile("(?<=key:)(\\S+)");
    private static final Pattern SECRET_PATTERN = Pattern.compile("(?<=secret:)(\\S+)");

    private UserService userService;

    @Override
    public String buildMessage(CommandParameters parameters) {
        Optional<ExchangeKey> exchangeKey = getExchangeKey(parameters.getUserInput());
        User user = parameters.getUser();

        return exchangeKey.map(k -> {
            addKeys(user, k);
            return "Keys added successfully";
        })
                .orElse("Please input correct keys");

    }

    private void addKeys(User user, ExchangeKey k) {
        user.getKeys().put(Exchange.EXMO, k);
        userService.update(user);
    }

    private Optional<ExchangeKey> getExchangeKey(String userInput) {
        Optional<String> key = getValue(KEY_PATTERN.matcher(userInput));
        Optional<String> secret = getValue(SECRET_PATTERN.matcher(userInput));

        return key.flatMap(k -> secret.map(s ->
                ExchangeKey.builder()
                        .key(k)
                        .secret(s)
                        .build()));
    }

    private Optional<String> getValue(Matcher matcher) {
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }
}
