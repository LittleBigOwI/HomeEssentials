package net.philocraft.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.philocraft.HomeEssentials;
import net.philocraft.utils.Worlds;

public class Home {

    private static HashMap<UUID, ArrayList<Home>> homes = new HashMap<>();
    
    private UUID uuid;
    private String name;
    private Location location;

    public Home(UUID uuid, String name, Location location) {
        
        //!SQL Injections are not possible here because the name of a Home object can't contain a space  
        try {
            HomeEssentials.getDatabase().createStatement(
                "INSERT INTO Homes(uuid, name, x, y, z, yaw, pitch) VALUES('" + 
                uuid + "', '" + 
                name + "', " +
                location.getX() + ", " +
                location.getY() + ", " +
                location.getZ() + ", " +
                location.getYaw() + ", " +
                location.getPitch() + ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.uuid = uuid;
        this.name = name;
        this.location = new Location(
            Worlds.OVERWORLD.getWorld(),
            location.getX(), 
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );

        if(homes.containsKey(uuid) && homes.get(uuid) != null && homes.get(uuid).size() != 0) {
            homes.get(uuid).add(this);
        } else {
            homes.put(uuid, new ArrayList<>(Arrays.asList(this)));
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        try {
            HomeEssentials.getDatabase().createStatement(
                "UPDATE Homes SET " + 
                "x=" + location.getX() + ", " +
                "y=" + location.getY() + ", " +
                "z=" + location.getZ() + ", " +
                "yaw=" + location.getYaw() + ", " +
                "pitch=" + location.getPitch() + " " +
                "WHERE uuid='" + this.uuid + "' " +
                "AND name='" + this.name + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        this.location = new Location(
            Worlds.OVERWORLD.getWorld(),
            location.getX(), 
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );
    }

    public static ArrayList<String> getHomeNames(Player player) {
        UUID uuid = player.getUniqueId();
        ArrayList<Home> playerHomes = homes.get(uuid);
        
        if(playerHomes == null) {
            homes.put(uuid, new ArrayList<Home>());
            playerHomes = homes.get(uuid);
        }

        ArrayList<String> homeNames = new ArrayList<>();
        for(Home home : playerHomes) {
            homeNames.add(home.getName());
        }

        return homeNames;
    }

    public static Home getHome(UUID uuid, String name) {
        if(!(homes.containsKey(uuid)) || homes.get(uuid) == null) {
            homes.put(uuid, new ArrayList<Home>());
        }
        
        for(Home home : homes.get(uuid)) {
            if(home.name.equals(name)) {
                return home;
            }
        }
        
        return null;
    }

}