package net.philocraft;

import org.bukkit.plugin.java.JavaPlugin;

import net.philocraft.commands.SethomeCommand;
import net.philocraft.models.Database;

public final class HomeEssentials extends JavaPlugin {

    private static Database database;

    public static Database getDatabase() {
        return database;
    }
    
    @Override
    public void onEnable() {
        database = Database.init(this);

        //!REGISTER COMMANDS
        this.getCommand("sethome").setExecutor(new SethomeCommand());
        
        this.getLogger().info("Plugin loaded");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin unloaded");
    }

}
