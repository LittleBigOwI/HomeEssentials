package net.philocraft;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import net.philocraft.commands.HomeCommand;
import net.philocraft.commands.HomesCommand;
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
        
        try {
            database.loadHomes();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //!REGISTER COMMANDS
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("homes").setExecutor(new HomesCommand());
        this.getCommand("sethome").setExecutor(new SethomeCommand());
        
        this.getLogger().info("Plugin loaded");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin unloaded");
    }

}
