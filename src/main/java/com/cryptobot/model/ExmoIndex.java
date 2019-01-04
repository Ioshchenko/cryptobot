package com.cryptobot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "exmo")
public class ExmoIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long none;
}
