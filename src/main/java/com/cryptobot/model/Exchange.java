package com.cryptobot.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Exchange {
    private String name;
    private String url;
    private Map<String, String> pairs;
}
