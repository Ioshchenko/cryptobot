package com.cryptobot.model.telegram;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateEntity {
    private int updateId;
    private ResponseMessage message;
}
