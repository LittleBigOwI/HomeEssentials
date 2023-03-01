package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class InvalidArgumentsException {
    
    private String cause;

    public InvalidArgumentsException() {
        this.cause = ChatColor.RED + "Invalid arguments.";
    }

    public InvalidArgumentsException(String cause) {
        this.cause = ChatColor.RED + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
