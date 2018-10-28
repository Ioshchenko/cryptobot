package com.cryptobot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message {
    private int messageId;
    private User from;
    private int date;
    private Chat chat;
    private String text;
}
