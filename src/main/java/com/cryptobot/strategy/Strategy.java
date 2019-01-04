package com.cryptobot.strategy;

public interface Strategy<T, R> {
    R execute(T t);
}
