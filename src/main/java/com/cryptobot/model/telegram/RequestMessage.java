package com.cryptobot.model.telegram;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessage {
    private int chatId;
    private String text;
    private int replyToMessageId;
    private String parseMode;
}
