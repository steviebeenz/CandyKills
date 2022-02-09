package tech.candy_dev.candykills.user;

import tech.candy_dev.candykills.CandyKills;
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
        Bukkit.getScheduler().runTaskAsynchronously(CandyKills.getInstance(), () -> {
            try {
                Connection connection = CandyKills.getInstance().getDatabase().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `candykills_userdata` WHERE `uuid` = ?;");
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
            Bukkit.getScheduler().runTaskAsynchronously(CandyKills.getInstance(), () -> {
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
        Connection connection = CandyKills.getInstance().getDatabase().getConnection();
        PreparedStatement presenceCheck = connection.prepareStatement("SELECT `uuid` FROM `candykills_userdata` WHERE `uuid` = ?");
        presenceCheck.setString(1, getUUID().toString());
        ResultSet resultSet = presenceCheck.executeQuery();
        PreparedStatement updateStatement;
        if (!resultSet.next()) {
            updateStatement = connection.prepareStatement("INSERT INTO `candykills_userdata` (`kills`, `deaths`, `killstreak`, `uuid`) VALUES (?,?,?,?);");
        } else {
            updateStatement = connection.prepareStatement("UPDATE `candykills_userdata` SET `kills` = ?, `deaths` = ?, `killstreak` = ? WHERE `uuid` = ?;");
        }
        updateStatement.setInt(1, getKills());
        updateStatement.setInt(2, getDeaths());
        updateStatement.setInt(3, getKillStreak());
        updateStatement.setString(4, getUUID().toString());
        updateStatement.execute();
        CandyKills.getInstance().sendConsoleMessage("Saved User: " + getUUID());
    }

}
