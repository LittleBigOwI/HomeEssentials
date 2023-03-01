package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class InvalidSenderException {
    
    private String cause;

    public InvalidSenderException() {
        this.cause = ChatColor.RED + "Invalid sender type.";
    }

    public InvalidSenderException(String cause) {
        this.cause = ChatColor.RED + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}