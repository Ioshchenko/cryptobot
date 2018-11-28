package com.cryptobot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
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
