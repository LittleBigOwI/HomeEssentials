package net.philocraft;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.littlebigowl.api.EssentialsAPI;
import net.philocraft.commands.DelhomeCommand;
import net.philocraft.commands.HomeCommand;
import net.philocraft.commands.HomesCommand;
import net.philocraft.commands.SethomeCommand;
import net.philocraft.utils.DatabaseUtil;

public final class HomeEssentials extends JavaPlugin {

    public static final EssentialsAPI api = (EssentialsAPI) Bukkit.getServer().getPluginManager().getPlugin("EssentialsAPI");
    
    private static HomeEssentials plugin;

    public static HomeEssentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        
        plugin = this;
        
        if(api == null) {
            this.getLogger().severe("Couldn't find API.");
        } else {
            
            try {
                DatabaseUtil.loadHomes();
            } catch (SQLException e) {
                this.getLogger().severe("Couldn't load homes from database : " + e.getMessage());
            }

        }

        //!REGISTER COMMANDS
        this.getCommand("delhome").setExecutor(new DelhomeCommand());
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("homes").setExecutor(new HomesCommand());
        this.getCommand("sethome").setExecutor(new SethomeCommand());
        
        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

}
