package com.cryptobot.kafka.producer;

import java.util.Locale;

public class PriceFormat {
    public static String format(String price) {
        return String.format(Locale.US, "%.8f", Double.valueOf(price));
    }
}
