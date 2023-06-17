package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Worlds;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import dev.littlebigowl.api.errors.InvalidWorldException;
import net.philocraft.errors.HomeNotFoundException;
import net.philocraft.models.Home;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("home"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player)sender;

        Home home;
        if(args.length == 1) {
            home = Home.getHome(player.getUniqueId(), args[0]);
        } else {
            home = Home.getHome(player.getUniqueId(), "home");
        }

        if(home == null) {
            return new HomeNotFoundException().sendCause(sender);
        }

        if(!player.getWorld().equals(Worlds.OVERWORLD.getWorld())) {
            return new InvalidWorldException("Homes are only available in the overworld.").sendCause(sender);
        }

        home.teleport();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equalsIgnoreCase("home") && args.length == 1) {
            return Home.getHomeNames((Player)sender);
        }

        return new ArrayList<>();
    }

}
