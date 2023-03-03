package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.philocraft.components.WarningComponent;
import net.philocraft.constants.Colors;
import net.philocraft.errors.InvalidArgumentsException;
import net.philocraft.errors.InvalidSenderException;
import net.philocraft.models.Home;

public class SethomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("sethome"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 1 && args.length != 2) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player) sender;
        String name = args[0];
        Location location = player.getLocation();

        if(name.contains("'") || name.contains("\\") || name.contains("\"")) {
            return new InvalidArgumentsException("Home names can't contain \\, ' or \" characters.").sendCause(sender);
        }

        if(Home.getHomeNames(player).contains(name)) {
            return new InvalidArgumentsException("You already have a home with that name.").sendCause(sender);
        }

        Home home = Home.getHome(player.getUniqueId(), name);
        
        if(home != null && args.length == 2) {
            if(args[1].equals("override")) {
                home.setLocation(location);
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully overridden your " + Colors.COMMON.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");

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
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully created new " + Colors.COMMON.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");

        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

}
