package com.cryptobot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Chat {
    private int id;
    private String type;
    private String title;
    private String firstName;
    private String lastName;
    private String username;
    private String description;
}
