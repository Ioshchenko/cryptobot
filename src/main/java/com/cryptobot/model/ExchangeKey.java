package com.cryptobot.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Builder
@Entity
@Table(name = "exchange_key")
public class ExchangeKey implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String exchangeKey;
    private String exchangeSecret;

    @ManyToOne
    @JoinColumn
    private User user;
}
