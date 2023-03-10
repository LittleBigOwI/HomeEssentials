package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.philocraft.constants.Colors;
import net.philocraft.errors.HomeNotFoundException;
import net.philocraft.errors.InvalidArgumentsException;
import net.philocraft.errors.InvalidSenderException;
import net.philocraft.models.Home;

public class HomesCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("homes"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 0) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player) sender;
        ArrayList<String> homeNames = Home.getHomeNames(player);
        
        if(homeNames.size() == 0) {
            return new HomeNotFoundException("You don't have any homes.").sendCause(sender);
        }

        StringBuffer message = new StringBuffer(
            Colors.COMMON.getChatColor() + "You have "  + 
            Colors.HIGHTLIGHT.getChatColor() + homeNames.size() + 
            Colors.COMMON.getChatColor() + " homes :\n" +
            Colors.WARNING.getChatColor()
        );

        for(int i = 0; i < homeNames.size(); i++) {
            message.append(homeNames.get(i));
            if(i != homeNames.size()-1) {
                message.append(Colors.OBFUSCATE.getChatColor() + ", " + Colors.WARNING.getChatColor());
            }
        }

        player.sendMessage(message.toString());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
    
}
