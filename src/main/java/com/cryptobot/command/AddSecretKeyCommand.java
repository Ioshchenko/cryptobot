package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.User;
import com.cryptobot.service.UserService;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class AddSecretKeyCommand implements Command {
    private static final Pattern KEY_PATTERN = Pattern.compile("(?<=key:)\\s*(\\S+)");
    private static final Pattern SECRET_PATTERN = Pattern.compile("(?<=secret:)\\s*(\\S+)");

    private UserService userService;

    @Override
    public String buildMessage(CommandParameters parameters) {
        Optional<ExchangeKey> exchangeKey = getExchangeKey(parameters);
        User user = parameters.getUser();

        return exchangeKey.map(k -> {
            addKeys(user, k);
            return "Keys added successfully";
        })
                .orElse("Please input correct key/secret");

    }

    private void addKeys(User user, ExchangeKey k) {
        user.getExchangeKey().put(Exchange.EXMO, k);
        userService.save(user);
    }

    private Optional<ExchangeKey> getExchangeKey(CommandParameters parameters) {
        Optional<String> key = getValue(KEY_PATTERN.matcher(parameters.getUserInput()));
        Optional<String> secret = getValue(SECRET_PATTERN.matcher(parameters.getUserInput()));

        return key.flatMap(k -> secret.map(s ->
                ExchangeKey.builder()
                        .exchangeKey(k)
                        .exchangeSecret(s)
                        .user(parameters.getUser())
                        .build()));
    }

    private Optional<String> getValue(Matcher matcher) {
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }
}
