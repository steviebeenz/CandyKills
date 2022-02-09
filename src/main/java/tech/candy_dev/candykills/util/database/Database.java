package tech.candy_dev.candykills.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private Connection connection;

    private boolean connected = false;

    private final String username, password, host, database;

    private final int port;

    public Database(String user, String password, String host, String database, int port) {
        this.username = user;
        this.password = password;
        this.host = host;
        this.database = database;
        this.port = port;
        try {
            openConnection();
            connected = true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isConnected()) {
            try {
                createTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() throws SQLException{
        String query = "CREATE TABLE IF NOT EXISTS `candykills_userdata` (`uuid` VARCHAR(36) NOT NULL, `kills` INT(11) NOT NULL, `deaths` INT(11) NOT NULL, `killstreak` INT(11) NOT NULL);";
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        preparedStatement.execute();
    }

    public void openConnection() throws SQLException,
            ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                        + host + ":" + port + "/" + database,
                username, password);
    }

    public Connection getConnection() {
        try {
            if (connection.isClosed() || connection == null) {
                openConnection();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }
}
