package com.cryptobot.model.telegram;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TelegramResponse {
    private boolean ok;
    List<UpdateEntity> result;
}
