package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.philocraft.errors.HomeNotFoundException;
import net.philocraft.errors.InvalidArgumentsException;
import net.philocraft.errors.InvalidSenderException;
import net.philocraft.models.Home;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("home"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player)sender;
        Home home = Home.getHome(player.getUniqueId(), args[0]);

        if(home == null) {
            return new HomeNotFoundException().sendCause(sender);
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
