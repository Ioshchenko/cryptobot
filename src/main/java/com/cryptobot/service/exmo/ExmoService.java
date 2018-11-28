package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.UserInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ExmoService {

    @Autowired
    private ExmoAuthApi exmoAuthApi;
    private ObjectMapper mapper;

    @PostConstruct
    private void init() {
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public UserInfo getUserInfo(User user) {
        String response = exmoAuthApi.request("/user_info", user.getExchangeKey().get(Exchange.EXMO));
        return mapper.convertValue(response, UserInfo.class);
    }
}
