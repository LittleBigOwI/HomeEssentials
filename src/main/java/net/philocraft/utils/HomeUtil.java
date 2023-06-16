package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;

import dev.littlebigowl.api.constants.Worlds;
import net.philocraft.HomeEssentials;
import net.philocraft.models.Home;

public class HomeUtil {
    
    public static void loadHomes() throws SQLException {

        HomeEssentials.api.database.create(
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

        ResultSet results = HomeEssentials.api.database.fetch("SELECT * FROM Homes;");

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

        HomeEssentials.getPlugin().getLogger().info("Loaded " + count + " homes.");
    }
}
