package net.philocraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class HomeEssentials extends JavaPlugin {
    
    @Override
    public void onEnable() {
        this.getLogger().info("Hello World!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Bye World!");
    }

}
