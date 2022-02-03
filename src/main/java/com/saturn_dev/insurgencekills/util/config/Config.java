package com.saturn_dev.insurgencekills.util.config;

import com.saturn_dev.insurgencekills.util.file.yaml.YamlFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public enum Config {

    SQL_ENABLED("sql.enabled", false),
    SQL_USERNAME("sql.username", "root"),
    SQL_PASSWORD("sql.password", "password"),
    SQL_PORT("sql.port", 3306),
    SQL_DATABASE("sql.database", "database"),
    SQL_HOST("sql.host", "localhost"),
    SAVE_INTERVAL("user.save-interval", 300),
    KILL_REWARD("user.kill-reward", 25D),
    DEATH_FINE("user.death-fine", 0D);

    private final String path;
    private Object value;

    Config(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public static void loadConfig(YamlFile iConfig) {

        boolean saveConfig = false;

        YamlConfiguration config = iConfig.getConfig();

        for (Config conf : values()) {
            if (!config.contains(conf.path)) {
                config.set(conf.path, conf.value);
                saveConfig = true;
                continue;
            }

            conf.value = config.get(conf.path);
        }

        if (saveConfig) {
            iConfig.saveConfig();
        }
    }

    public Object getValue() {
        return this.value;
    }

    public String getString() {
        return (String) this.value;
    }

    public boolean getBoolean() {
        return (Boolean) this.value;
    }

    public int getInt() {
        return (Integer) this.value;
    }

    public double getDouble() {
        return (Double) this.value;
    }

    public List<String> getStringList() {
        return (List<String>) this.value;
    }

}
