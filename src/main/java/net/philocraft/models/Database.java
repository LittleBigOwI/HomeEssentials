package net.philocraft.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.ConfigurationSection;

import net.philocraft.HomeEssentials;

public class Database {
    
    private String host;
    private String database;
    private String user;
    private String password;

    private Connection connection;
    private HomeEssentials plugin;

    private Database(HomeEssentials plugin, String host, String database, String user, String password) throws ClassNotFoundException, SQLException {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
        
    }

    private void resetConnection() throws SQLException {
        this.connection.close();
        this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.database, this.user, this.password);
    }

    public static Database init(HomeEssentials homeEssentials) {
        homeEssentials.saveDefaultConfig();
        ConfigurationSection credentials = homeEssentials.getConfig().getConfigurationSection("database");

        Database database = null;
        try {
            database = new Database(
                homeEssentials,
                credentials.getString("host"), 
                credentials.getString("database"), 
                credentials.getString("user"), 
                credentials.getString("password")
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return database;
    }

    public int createStatement(String sql) throws SQLException {
        this.resetConnection();
        return this.connection.createStatement().executeUpdate(sql);
    }

}