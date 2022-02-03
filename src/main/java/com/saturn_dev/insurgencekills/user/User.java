package com.saturn_dev.insurgencekills.user;

import com.saturn_dev.insurgencekills.InsurgenceKills;

import java.util.UUID;

public abstract class User {

    private final UUID uuid;

    private int kills, deaths, killStreak;

    public User(UUID uuid) {
        this.uuid = uuid;
        load();
    }

    public abstract void load();

    public abstract void save(boolean async);

    public int getKills() {
        return kills;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setKills(int kills, boolean save) {
        this.kills = kills;
        saveData(save);
    }

    public void setDeaths(int deaths, boolean save) {
        this.deaths = deaths;
        saveData(save);
    }

    public void setKillStreak(int killStreak, boolean save) {
        this.killStreak = killStreak;
        saveData(save);
    }

    private void saveData(boolean save) {
        if (!save) {
            return;
        }
        if (InsurgenceKills.getInstance().getUserManager().isPendingSave(this)) {
            return;
        }
        InsurgenceKills.getInstance().getUserManager().addPendingSave(this);

    }

    public UUID getUUID() {
        return uuid;
    }


}
