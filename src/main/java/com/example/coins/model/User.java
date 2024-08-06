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
    private String name;

    @Column
    private long refId;

    @Column
    private long coins;

    @Column
    private int refCount;

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

    public void incRefCount() {
        this.refCount += 1;
    }
}
