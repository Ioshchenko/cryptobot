package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExmoService {

    @Autowired
    private ExmoAuthApi exmoAuthApi;
    private ObjectMapper mapper = new ObjectMapper();

    public UserInfo getUserInfo(User user) {
        String response = exmoAuthApi.request("/user_info", user.getExchangeKey().get(Exchange.EXMO));
        return mapper.convertValue(response, UserInfo.class);
    }
}
