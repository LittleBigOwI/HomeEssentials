package net.philocraft.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.philocraft.HomeEssentials;
import net.philocraft.constants.Worlds;

public class Home {

    private static HashMap<UUID, ArrayList<Home>> homes = new HashMap<>();
    
    private UUID uuid;
    private String name;
    private Location location;

    public Home(UUID uuid, String name, Location location, boolean insert) {

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

        if(insert) {
            //!SQL Injections are not possible here because the name of a Home object can't contain a space  
            try {
                HomeEssentials.getDatabase().updateStatement(
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

            Home.putHome(uuid, this); 
        }
        
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

    public static void putHome(UUID uuid, Home home) {
        if(homes.containsKey(uuid) && homes.get(uuid) != null && homes.get(uuid).size() != 0) {
            homes.get(uuid).add(home);
        } else {
            homes.put(uuid, new ArrayList<>(Arrays.asList(home)));
        }
    }

    public static int getMaxHomes(Player player) {
        int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
        HashMap<Integer, Integer> rankHomes = HomeEssentials.getDatabase().getRankHomes();
        
        int maxHomes = 0; 
        for(Integer maxHome : rankHomes.values()) {
            if(maxHome > maxHomes) { maxHomes = maxHome; }
        }
        
        for(Integer homePlayTime : rankHomes.keySet()) {
            if(playtime <= homePlayTime && rankHomes.get(homePlayTime) < maxHomes) {
                maxHomes = rankHomes.get(homePlayTime);
            }
        }
        
        return maxHomes;
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
            HomeEssentials.getDatabase().updateStatement(
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

    public void teleport() {
        Player player = Bukkit.getPlayer(this.uuid);
        player.teleport(this.location);
    }

    public void delete() {
        try {
            HomeEssentials.getDatabase().updateStatement(
                "DELETE FROM Homes WHERE " +
                "uuid='" + this.uuid + "' AND " +
                "name ='" + this.name + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Home> playerHomes = Home.homes.get(this.uuid);
        for(int i = 0; i < playerHomes.size(); i++) {
            if(playerHomes.get(i).getName().equals(this.name)) {
                playerHomes.remove(i);
            }
        }

    }

}