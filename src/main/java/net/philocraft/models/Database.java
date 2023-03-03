package net.philocraft.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.philocraft.HomeEssentials;
import net.philocraft.constants.Worlds;

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

    public int updateStatement(String sql) throws SQLException {
        this.resetConnection();
        return this.connection.createStatement().executeUpdate(sql);
    }

    public ResultSet fetchStatement(String sql) throws SQLException {
        this.resetConnection();
        return this.connection.prepareStatement(sql).executeQuery();
    }

    public void loadHomes() throws SQLException {
        ResultSet results = this.fetchStatement("SELECT * FROM Homes;");

        int count = 0;
        while(!results.isClosed() && !results.isLast()) {
            results.next();
            
            UUID uuid = UUID.fromString(results.getString("uuid"));
            String name = results.getString("name");
            Location location = new Location(
                Worlds.OVERWORLD.getWorld(),
                results.getFloat("x"),
                results.getFloat("y"),
                results.getFloat("z"),
                results.getFloat("yaw"),
                results.getFloat("pitch")
            );

            Home.putHome(uuid, new Home(uuid, name, location, false));
            count++;
        }

        this.plugin.getLogger().info("Loaded " + count + " homes.");
    }

}