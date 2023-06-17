package net.philocraft.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2i;

import de.bluecolored.bluemap.api.AssetStorage;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import dev.littlebigowl.api.constants.Worlds;
import net.philocraft.HomeEssentials;

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
                HomeEssentials.api.database.update(
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

        this.draw();
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
        return HomeEssentials.api.scoreboard.getEssentialsTeam(player).getMaxHomes();
    }

    public static ArrayList<Home> getHomes() {
        ArrayList<Home> allHomes = new ArrayList<>();

        for(ArrayList<Home> playerHomes : homes.values()) {
            for(Home home : playerHomes) {
                allHomes.add(home);
            }
        }

        return allHomes;
    }

    public UUID getUUID() {
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
            HomeEssentials.api.database.update(
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
        this.erase();
        try {
            HomeEssentials.api.database.update(
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

    public void draw() {

        UUID uuid = this.getUUID();
        String playerName;

        if(Bukkit.getPlayer(uuid) != null) {
            playerName = Bukkit.getPlayer(uuid).getName();
        } else {
            playerName = Bukkit.getOfflinePlayer(uuid).getName();
        }

        POIMarker marker = POIMarker.builder()
            .label(this.getName())
            .detail("<div style=\"text-align: center\">" + this.getName() + "</div>" + "(" + playerName + ")")
            .minDistance(0.0)
            .maxDistance(3300)
            .position(this.location.getX(), this.location.getY(), this.location.getZ())
            .build();

        HomeEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {
                AssetStorage assetStorage = map.getAssetStorage();
                String icon = assetStorage.getAssetUrl("markericons/home.png");

                marker.setIcon(icon, new Vector2i(12, 24));

                if(map.getMarkerSets().get("Homes") != null) {
                    map.getMarkerSets().get("Homes").put(this.getName() + "-" + this.getUUID(), marker);

                } else {
                    MarkerSet markerSet = MarkerSet.builder().label("Homes").build();
                    map.getMarkerSets().put("Homes", markerSet);
                    map.getMarkerSets().get("Homes").put(this.getName() + "-" + this.getUUID(), marker);

                }
            }
        });
    }

    public void erase() {
        HomeEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {         
                map.getMarkerSets().get("Homes").remove(this.getName());
            }
        });
    }

}