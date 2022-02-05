package com.dan_owens.insurgencekills.user.manager;

import com.dan_owens.insurgencekills.InsurgenceKills;
import com.dan_owens.insurgencekills.user.JSONUser;
import com.dan_owens.insurgencekills.util.file.gson.GsonUtil;
import com.dan_owens.insurgencekills.user.SQLUser;
import com.dan_owens.insurgencekills.user.User;
import com.dan_owens.insurgencekills.util.config.Config;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserManager {

    private Map<UUID, User> userMap;

    private Queue<UUID> pendingSave;

    public UserManager() {
        loadUsers();
        this.pendingSave = new PriorityQueue<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(InsurgenceKills.getInstance(), () -> save(true), Config.SAVE_INTERVAL.getInt() * 20L, Config.SAVE_INTERVAL.getInt() * 20L);
    }

    private void loadUsers() {
        this.userMap = new HashMap<>();
        if (Config.SQL_ENABLED.getBoolean()) {
            Connection connection = InsurgenceKills.getInstance().getDatabase().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT `uuid` FROM `insurgencekills_userdata`");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    userMap.put(uuid, new SQLUser(uuid));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        File folder = new File(InsurgenceKills.getInstance().getDataFolder(), "user");

        if (!folder.exists()) {
            folder.mkdir();
            return;
        }

        if (folder.listFiles() == null) {
            return;
        }

        for (File file : folder.listFiles()) {
            String name = file.getName();
            userMap.put(UUID.fromString(name.split("\\.")[0]), GsonUtil.read(folder, name, JSONUser.class));
        }

    }

    public boolean isPendingSave(User user) {
        return pendingSave.contains(user.getUUID());
    }

    public void addPendingSave(User user) {
        pendingSave.add(user.getUUID());
    }

    public void save(boolean async) {
        while (pendingSave.peek() != null) {
            User user = getUser(pendingSave.poll());
            user.save(async);
        }
    }

    public void registerUser(UUID id) {
        if (Config.SQL_ENABLED.getBoolean()) {
            userMap.put(id, new SQLUser(id));
            return;
        }
        userMap.put(id, new JSONUser(id));
    }

    public User getUser(UUID id) {
        return userMap.get(id);
    }

}
