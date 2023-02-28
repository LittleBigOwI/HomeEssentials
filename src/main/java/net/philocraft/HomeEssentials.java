package net.philocraft;

import org.bukkit.plugin.java.JavaPlugin;

import net.philocraft.models.Database;

public final class HomeEssentials extends JavaPlugin {

    private static Database database;

    public static Database getDatabase() {
        return database;
    }
    
    @Override
    public void onEnable() {
        database = Database.init(this);
        
        this.getLogger().info("Hello World!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Bye World!");
    }

}
