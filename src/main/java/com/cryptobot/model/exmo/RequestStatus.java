package com.cryptobot.model.exmo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestStatus {
    private boolean result;
    private String error;
    private long orderId;
}
