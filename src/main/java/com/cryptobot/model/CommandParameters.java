package com.cryptobot.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommandParameters {
    private String userInput;
    private User user;
}
