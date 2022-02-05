package com.dan_owens.insurgencekills.user;

import com.dan_owens.insurgencekills.InsurgenceKills;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLUser extends User {

    public SQLUser(UUID id) {
        super(id);
    }

    @Override
    public void load() {
        Bukkit.getScheduler().runTaskAsynchronously(InsurgenceKills.getInstance(), () -> {
            try {
                Connection connection = InsurgenceKills.getInstance().getDatabase().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `insurgencekills_userdata` WHERE `uuid` = ?;");
                preparedStatement.setString(1, getUUID().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    setKills(resultSet.getInt("kills"), false);
                    setDeaths(resultSet.getInt("deaths"), false);
                    setKillStreak(resultSet.getInt("killstreak"), false);
                } else {
                    setKills(0, true);
                    setDeaths(0, true);
                    setKillStreak(0, true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void save(boolean async) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(InsurgenceKills.getInstance(), () -> {
                try {
                    save();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void save() throws SQLException {
        Connection connection = InsurgenceKills.getInstance().getDatabase().getConnection();
        PreparedStatement presenceCheck = connection.prepareStatement("SELECT `uuid` FROM `insurgencekills_userdata` WHERE `uuid` = ?");
        presenceCheck.setString(1, getUUID().toString());
        ResultSet resultSet = presenceCheck.executeQuery();
        PreparedStatement updateStatement;
        if (!resultSet.next()) {
            updateStatement = connection.prepareStatement("INSERT INTO `insurgencekills_userdata` (`kills`, `deaths`, `killstreak`, `uuid`) VALUES (?,?,?,?);");
        } else {
            updateStatement = connection.prepareStatement("UPDATE `insurgencekills_userdata` SET `kills` = ?, `deaths` = ?, `killstreak` = ? WHERE `uuid` = ?;");
        }
        updateStatement.setInt(1, getKills());
        updateStatement.setInt(2, getDeaths());
        updateStatement.setInt(3, getKillStreak());
        updateStatement.setString(4, getUUID().toString());
        updateStatement.execute();
        InsurgenceKills.getInstance().sendConsoleMessage("Saved User: " + getUUID());
    }

}
