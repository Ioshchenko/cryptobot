package com.cryptobot.kafka;

public class Exchange {

    public static class Bitfinex {
        public static final String URL = "https://api.bitfinex.com/v2/tickers?symbols=ALL";

        public static class Pair {
            public static final String BTC_USD = "tBTCUSD";
        }
    }

    public static class Exmo {
        public static final String URL = "https://api.exmo.com/v1/ticker/";

        public static class Pair {
            public static final String BTC_USD = "BTC_USD";
        }

    }
}
