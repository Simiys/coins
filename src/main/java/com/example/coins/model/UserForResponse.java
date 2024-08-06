package com.example.coins.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserForResponse {
    private long tgId;

    private String name;

    private long refId;

    private long coins;

    private int refCount;

    private long tickets;

    private long coinsFromRefs;

    private long earnedCoins;

    private boolean refLinkStatus;

    private int speedLevel;

    private int volumeLevel;

    private int timeLevel;

    @Nullable
    private String lastFarmStart;

    private String lastRefClaim;

    public UserForResponse (User user) {
        this.coins = user.getCoins();
        this.refCount = user.getRefCount();
        this.tgId = user.getTgId();
        this.name = user.getName();
        this.refId = user.getRefId();
        this.tickets = user.getTickets();
        this.coinsFromRefs = user.getCoinsFromRefs();
        this.earnedCoins = user.getEarnedCoins();
        this.refLinkStatus = user.isRefLinkStatus();
        this.speedLevel=user.getSpeedLevel();
        this.volumeLevel=user.getVolumeLevel();
        this.timeLevel=user.getTimeLevel();
        this.lastFarmStart = user.getLastFarmStart() == null ?  null :  user.getLastFarmStart().toString();
        this.lastRefClaim = user.getLastRefClaim() == null ?  null :  user.getLastRefClaim().toString();

    }

}
