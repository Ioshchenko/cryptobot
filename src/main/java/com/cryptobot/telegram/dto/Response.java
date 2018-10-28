package com.cryptobot.telegram.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Response {
    private boolean ok;
    List<UpdateEntity> result;
}
