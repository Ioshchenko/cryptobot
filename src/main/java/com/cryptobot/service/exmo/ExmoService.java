package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExmoService {

    @Autowired
    private ExmoAuthApi exmoAuthApi;

    public UserInfo getUserInfo(User user) {
        return (UserInfo) exmoAuthApi.request("/user_info", user.getExchangeKey().get(Exchange.EXMO), UserInfo.class);
    }
}
