package com.example.coins.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private long tgId;

    @Column
    private long refId;

    @Column
    private long coins;

    @Column
    private long tickets;

    @Column
    private long coinsFromRefs;

    @Column
    private long earnedCoins;

    @Column
    private boolean refLinkStatus;

    @Column
    private int speedLevel;

    @Column
    private int volumeLevel;

    @Column
    private int timeLevel;

    @Column
    private LocalDateTime lastFarmStart;

    @Column
    private LocalDateTime lastRefClaim;



    public User() {

    }

    public User(long tgId) {
        this.tgId = id;
        this.coins = 0;
        this.coinsFromRefs = 0;
        this.earnedCoins = 0;
        this.refLinkStatus = false;
        this.volumeLevel = 0;
        this.timeLevel = 0;
        this.speedLevel = 0;
    }
}
