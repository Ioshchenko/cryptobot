package com.cryptobot.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Exchange {
    private String name;
    private String url;
}
