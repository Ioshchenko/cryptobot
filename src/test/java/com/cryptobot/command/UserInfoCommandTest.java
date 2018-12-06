package com.cryptobot.command;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.UserInfo;
import com.cryptobot.service.exmo.ExmoService;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoCommandTest {

    @Mock
    private ExmoService exmoService;
    private static Configuration config;
    private UserInfoCommand command;


    @BeforeAll
    static void init() {
        config = new Configuration(Configuration.VERSION_2_3_28);
        config.setClassForTemplateLoading(UserInfoCommandTest.class, "/templates");
    }

    @Test
    void shouldBuildMissingKeyMessage() {
        command = new UserInfoCommand(config, exmoService);

        String message = command.buildMessage(CommandParameters.builder()
                .user(new User())
                .build());

        assertTrue(message.contains("Missing yours api keys"));
    }

    @Test
    void shouldBuildBMessage() {
        User user = new User();
        user.setExchangeKey(getExmoExchangeKey());
        CommandParameters parameters = CommandParameters.builder().user(user).build();
        UserInfo userInfo = getUserInfo();

        when(exmoService.getUserInfo(user)).thenReturn(userInfo);

        command = new UserInfoCommand(config, exmoService);
        String message = command.buildMessage(parameters);

        assertTrue(message.contains("USD = 200.52"), "Balance not present");
        assertTrue(message.contains("BTC = 0.34"), "Reserved not present");
    }

    private UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        Map<String, Double> balances = new HashMap<>();
        balances.put("USD", 200.52);
        userInfo.setBalances(balances);

        Map<String, Double> reserved = new HashMap<>();
        reserved.put("BTC", 0.34);
        userInfo.setReserved(reserved);
        return userInfo;
    }

    private Map<String, ExchangeKey> getExmoExchangeKey() {
        Map<String, ExchangeKey> keys = new HashMap<>();
        ExchangeKey key = new ExchangeKey();
        key.setExchangeKey("key");
        key.setExchangeSecret("secret");
        keys.put(Exchange.EXMO, key);
        return keys;
    }

}