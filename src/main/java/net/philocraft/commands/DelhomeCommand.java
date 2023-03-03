package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.philocraft.components.WarningComponent;
import net.philocraft.constants.Colors;
import net.philocraft.errors.HomeNotFoundException;
import net.philocraft.errors.InvalidArgumentsException;
import net.philocraft.errors.InvalidSenderException;
import net.philocraft.models.Home;

public class DelhomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player && label.equalsIgnoreCase("delhome"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 1 && args.length != 2) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player)sender;
        Home home = Home.getHome(player.getUniqueId(), args[0]);

        if(home == null) {
            return new HomeNotFoundException().sendCause(sender);
        }
        
        if(args.length == 2) {
            if(args[1].equals("confirm")) {
                home.delete();
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully deleted your " + Colors.COMMON.getChatColor() + home.getName() + Colors.SUCCESS.getChatColor() + " home.");

            } else if(args[1].equals("cancel")) {
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully canceled deletion.");

            } else {
                return new InvalidArgumentsException().sendCause(sender);
            }

        } else if(args.length == 1){
            new WarningComponent(
                player,
                new String[]{"You are about to delete your ", home.getName(), " home. Proceed? "},
                "/delhome " + home.getName() + " confirm",
                "/delhome " + home.getName() + " cancel"
            ).send();
            
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equalsIgnoreCase("delhome") && args.length == 1) {
            return Home.getHomeNames((Player)sender);
        }

        return new ArrayList<>();
    }
    
}
