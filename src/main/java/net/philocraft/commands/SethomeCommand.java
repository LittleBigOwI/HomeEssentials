package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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

        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player) sender;
        String name = args[0];
        Location location = player.getLocation();

        if(name.contains("'") || name.contains("\\") || name.contains("\"")) {
            return new InvalidArgumentsException("Home names can't contain \\, ' or \" characters.").sendCause(sender);
        }

        if(Home.getHome(player.getUniqueId(), name) != null) {
            //!Override
        }

        Home home = new Home(player.getUniqueId(), name, location);

        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully created new " + Colors.WARNING.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

}
