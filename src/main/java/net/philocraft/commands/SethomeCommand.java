package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.components.WarningComponent;
import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.constants.Worlds;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import dev.littlebigowl.api.errors.InvalidWorldException;
import net.philocraft.errors.MaxHomesException;
import net.philocraft.models.Home;

public class SethomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("sethome"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        Player player = (Player) sender;

        if(!player.getWorld().equals(Worlds.OVERWORLD.getWorld())) {
            return new InvalidWorldException("Homes are only available in the overword.").sendCause(sender);
        }

        if(args.length != 1 && args.length != 2) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        String name = args[0];
        Location location = player.getLocation();

        if(name.contains("'") || name.contains("\\") || name.contains("\"")) {
            return new InvalidArgumentsException("Home names can't contain \\, ' or \" characters.").sendCause(sender);
        }

        if(Home.getHomeNames(player).size() >= Home.getMaxHomes(player) && Home.getMaxHomes(player) != -1) {
            return new MaxHomesException().sendCause(sender);
        }

        Home home = Home.getHome(player.getUniqueId(), name);
        
        if(home != null && args.length == 2) {
            if(args[1].equals("override")) {
                home.setLocation(location);
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully overridden your " + Colors.SUCCESS_DARK.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");

            } else if(args[1].equals("cancel")) {
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully canceled override.");

            } else {
                return new InvalidArgumentsException().sendCause(sender);
            }

        } else if(home != null){
            new WarningComponent(
                player,
                new String[]{"You are about to override your ", home.getName(), " home. Proceed? "},
                "/sethome " + home.getName() + " override",
                "/sethome " + home.getName() + " cancel"
            ).send();
            
        } else {
            home = new Home(player.getUniqueId(), name, location, true);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully created new " + Colors.SUCCESS_DARK.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");

        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

}
