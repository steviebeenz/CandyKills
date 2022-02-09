package tech.candy_dev.candykills.user;

import tech.candy_dev.candykills.CandyKills;
import tech.candy_dev.candykills.util.file.gson.GsonUtil;

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
        GsonUtil.save(new File(CandyKills.getInstance().getDataFolder(), "user"), getUUID().toString(), this);
    }
}
