package net.philocraft;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.bluecolored.bluemap.api.BlueMapAPI;
import dev.littlebigowl.api.EssentialsAPI;
import net.philocraft.commands.DelhomeCommand;
import net.philocraft.commands.HomeCommand;
import net.philocraft.commands.HomesCommand;
import net.philocraft.commands.SethomeCommand;
import net.philocraft.utils.HomeUtil;

public final class HomeEssentials extends JavaPlugin {

    public static final EssentialsAPI api = (EssentialsAPI) Bukkit.getServer().getPluginManager().getPlugin("EssentialsAPI");
    public static BlueMapAPI blueMap;
    
    private static HomeEssentials plugin;

    public static HomeEssentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        BlueMapAPI.onEnable(bluemap -> {
            blueMap = bluemap;
            this.getLogger().info("Loaded BlueMapAPI.");

            if(api == null) {
                this.getLogger().severe("Couldn't find API.");

            } else {
                try {
                    HomeUtil.loadHomes();
                } catch (SQLException e) {
                    this.getLogger().severe("Couldn't load homes from database : " + e.getMessage());
                }
            }
        });
        
        plugin = this;

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
