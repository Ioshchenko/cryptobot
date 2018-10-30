package com.cryptobot.model.telegram;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private int id;
    private boolean isBot;
    private String firstName;
    private String lastName;
    private String username;
    private String languageCode;
}
