package net.philocraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;

public enum Worlds {

    OVERWORLD(Bukkit.getWorld("world")),
    NETHER(Bukkit.getWorld("world_nether")),
    END(Bukkit.getWorld("world_the_end"));

    private World world;

    private Worlds(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }
}
