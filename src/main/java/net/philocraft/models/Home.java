package net.philocraft.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import net.philocraft.utils.Worlds;

public class Home {

    private static HashMap<UUID, List<Home>> homes = new HashMap<>();
    
    private UUID uuid;
    private String name;
    private Location location;

    public Home(UUID uuid, String name, Location location) {
        //SQL stuff

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
            homes.put(uuid, Arrays.asList(this));
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
        //SQL stuff
        
        this.location = new Location(
            Worlds.OVERWORLD.getWorld(),
            location.getX(), 
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );
    }

}