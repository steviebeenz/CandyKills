package com.saturn_dev.insurgencekills.user;

import com.saturn_dev.insurgencekills.InsurgenceKills;
import com.saturn_dev.insurgencekills.util.file.gson.GsonUtil;

import java.io.File;
import java.util.UUID;

public class JSONUser extends User {

    public JSONUser(UUID uuid) {
        super(uuid);
    }

    @Override
    public void load() {
        setKills(0, true);
        setDeaths(0, true);
        setKillStreak(0, true);
    }

    @Override
    public void save(boolean async) {
        GsonUtil.save(new File(InsurgenceKills.getInstance().getDataFolder(), "user"), getUUID().toString(), this);
    }
}
