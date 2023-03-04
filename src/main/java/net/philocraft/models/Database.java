package net.philocraft.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.philocraft.HomeEssentials;
import net.philocraft.constants.Worlds;

public class Database {

    private static HashMap<Integer, Integer> rankHomes = new HashMap<>();
    
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

        this.createStatement(
            "CREATE TABLE IF NOT EXISTS Homes(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " + 
            "uuid TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "x FLOAT NOT NULL, " +
            "y FLOAT NOT NULL, " +
            "z FLOAT NOT NULL, " +
            "yaw FLOAT NOT NULL, " +
            "pitch FLOAT NOT NULL);"
        );
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

        List<?> cRankPlaytime = homeEssentials.getConfig().getList("rankPlaytime");
        List<?> cRankHomes = homeEssentials.getConfig().getList("rankHomes");

        if(cRankPlaytime.size() != cRankHomes.size()) {
            homeEssentials.getLogger().warning("Invalid rank configuration, switching to default config.");
            
            cRankPlaytime = new ArrayList<>(Arrays.asList(500, 1000, 1500, 2000, 2500));
            cRankHomes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        }

        for(int i = 0; i < cRankPlaytime.size(); i++) {
            Database.rankHomes.put(
                Integer.parseInt(cRankPlaytime.get(i).toString()),
                Integer.parseInt(cRankHomes.get(i).toString())
            );
        }

        return database;
    }

    public boolean createStatement(String sql) throws SQLException {
        this.resetConnection();
        return this.connection.createStatement().execute(sql);
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
        while(results.next()) {
            
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

    public HashMap<Integer, Integer> getRankHomes() {
        return Database.rankHomes;
    }

}